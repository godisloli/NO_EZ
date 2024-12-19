
package net.tiramisu.noez.item.arsenal;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MechanicalCrossbow extends CrossbowItem implements Vanishable {
    private static final String TAG_CHARGED = "Charged";
    private static final String TAG_CHARGED_PROJECTILES = "ChargedProjectiles";
    private static final int MAX_CHARGE_DURATION = 50;
    public static final int DEFAULT_RANGE = 8;
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final float START_SOUND_PERCENT = 0.2F;
    private static final float MID_SOUND_PERCENT = 0.5F;
    private static final float ARROW_POWER = 2F;
    private static final float FIREWORK_POWER = 1.5F;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public MechanicalCrossbow(Item.Properties pProperties) {
        super(pProperties);
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }


    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (isCharged(itemstack)) {
            performShooting(pLevel, pPlayer, pHand, itemstack, getShootingPower(itemstack), 1.0F);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
            if (!isCharged(itemstack)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                pPlayer.startUsingItem(pHand);
            }

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private static float getShootingPower(ItemStack pCrossbowStack) {
        return containsChargedProjectile(pCrossbowStack, Items.FIREWORK_ROCKET) ? FIREWORK_POWER : ARROW_POWER;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        int i = this.getUseDuration(pStack) - pTimeLeft;
        float f = getPowerForTime(i, pStack);
        if (f >= 1.0F && !isCharged(pStack) && tryLoadProjectiles(pEntityLiving, pStack)) {
            setCharged(pStack, true);
            SoundSource soundsource = pEntityLiving instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            pLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * MID_SOUND_PERCENT + 1.0F) + START_SOUND_PERCENT);
        }
    }

    private static boolean tryLoadProjectiles(LivingEntity pShooter, ItemStack pCrossbowStack) {
        int maxProjectiles = 4;
        boolean flag = pShooter instanceof Player && ((Player) pShooter).getAbilities().instabuild;
        List<ItemStack> projectilesToLoad = Lists.newArrayList();

        ItemStack itemstack = pShooter.getProjectile(pCrossbowStack);
        while (!itemstack.isEmpty() && projectilesToLoad.size() < maxProjectiles) {
            projectilesToLoad.add(itemstack.copy());
            if (!flag) {
                itemstack.shrink(1);
                if (itemstack.isEmpty() && pShooter instanceof Player) {
                    ((Player) pShooter).getInventory().removeItem(itemstack);
                }
            }
        }
        if (projectilesToLoad.isEmpty()) {
            return false;
        }
        for (int i = 0; i < projectilesToLoad.size(); i++) {
            if (!loadProjectile(pShooter, pCrossbowStack, projectilesToLoad.get(i), i > 0, flag)) {
                return i > 0;
            }
        }
        return true;
    }

    private static boolean loadProjectile(LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, boolean pHasAmmo, boolean pIsCreative) {
        if (pAmmoStack.isEmpty()) {
            return false;
        } else {
            boolean flag = pIsCreative && pAmmoStack.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !pIsCreative && !pHasAmmo) {
                itemstack = pAmmoStack.split(1);
            } else {
                itemstack = pAmmoStack.copy();
            }
            addChargedProjectile(pCrossbowStack, itemstack);
            return true;
        }
    }

    public static boolean isCharged(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null && compoundtag.getBoolean(TAG_CHARGED);
    }

    public static void setCharged(ItemStack pCrossbowStack, boolean pIsCharged) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        compoundtag.putBoolean(TAG_CHARGED, pIsCharged);
    }

    private static void addChargedProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains(TAG_CHARGED_PROJECTILES, 9)) {
            listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 10);
        } else {
            listtag = new ListTag();
        }
        CompoundTag compoundtag1 = new CompoundTag();
        pAmmoStack.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put(TAG_CHARGED_PROJECTILES, listtag);
        if (pAmmoStack.is(Items.FIREWORK_ROCKET)) {
            compoundtag.putBoolean("Firework", true);
        } else {
            compoundtag.putBoolean("Firework", false);
        }
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack pCrossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null && compoundtag.contains(TAG_CHARGED_PROJECTILES, 9)) {
            ListTag listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag1 = listtag.getCompound(i);
                list.add(ItemStack.of(compoundtag1));
            }
        }
        return list;
    }


    private static void clearChargedProjectiles(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 9);
            listtag.clear();
            compoundtag.put(TAG_CHARGED_PROJECTILES, listtag);
        }

    }

    public static boolean containsChargedProjectile(ItemStack pCrossbowStack, Item pAmmoItem) {
        return getChargedProjectiles(pCrossbowStack).stream().anyMatch((itemStack) -> itemStack.is(pAmmoItem));
    }

    private static void shootProjectile(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle) {
        if (!pLevel.isClientSide) {
            boolean flag = pAmmoStack.is(Items.FIREWORK_ROCKET);
            Projectile projectile;
            if (flag) {
                projectile = new FireworkRocketEntity(pLevel, pAmmoStack, pShooter, pShooter.getX(), pShooter.getEyeY() - (double)0.15F, pShooter.getZ(), true);
            } else {
                projectile = getArrow(pLevel, pShooter, pCrossbowStack, pAmmoStack);
                if (pIsCreativeMode || pProjectileAngle != 0.0F) {
                    ((AbstractArrow)projectile).pickup = Pickup.CREATIVE_ONLY;
                }
            }

            if (pShooter instanceof CrossbowAttackMob crossbowAttackMob) {
                crossbowAttackMob.shootCrossbowProjectile(crossbowAttackMob.getTarget(), pCrossbowStack, projectile, pProjectileAngle);
            } else {
                Vec3 vec31 = pShooter.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(pProjectileAngle * ((float)Math.PI / 180F), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = pShooter.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), pVelocity, pInaccuracy);
            }

            pCrossbowStack.hurtAndBreak(1, pShooter, (livingEntity) -> livingEntity.broadcastBreakEvent(pHand));
            pLevel.addFreshEntity(projectile);
            pLevel.playSound(null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }

    }

    private static AbstractArrow getArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(pAmmoStack.getItem() instanceof ArrowItem ? pAmmoStack.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmoStack, pLivingEntity);
        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }
        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        return abstractarrow;
    }

    public static void performShooting(Level pLevel, LivingEntity pShooter, InteractionHand pUsedHand, ItemStack pCrossbowStack, float pVelocity, float pInaccuracy) {
        if (pShooter instanceof Player player && pLevel instanceof ServerLevel) {
            if (ForgeEventFactory.onArrowLoose(pCrossbowStack, pShooter.level(), player, 1, true) < 0) {
                return;
            }
        }

        List<ItemStack> projectiles = getChargedProjectiles(pCrossbowStack);
        int delayPerShot = 2;

        for (int i = 0; i < projectiles.size(); i++) {
            final int index = i;
            final ItemStack itemstack = projectiles.get(i);
            final boolean isCreativeMode = pShooter instanceof Player && ((Player) pShooter).getAbilities().instabuild;
            final int delayTicks = index * delayPerShot;

            scheduler.schedule(() -> {
                if (!itemstack.isEmpty()) {
                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, 1.0F, isCreativeMode, pVelocity, pInaccuracy, 0.0F);
                }

                if (index == projectiles.size() - 1) {
                    onCrossbowShot(pLevel, pShooter, pCrossbowStack);
                    pLevel.playSound(null, pShooter.getX(), pShooter.getY(), pShooter. getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1, 1);
                }
            }, delayTicks * 50L, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
    }

    private static float[] getShotPitches(RandomSource pRandom) {
        boolean flag = pRandom.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, pRandom), getRandomShotPitch(!flag, pRandom)};
    }

    private static float getRandomShotPitch(boolean pIsHighPitched, RandomSource pRandom) {
        float f = pIsHighPitched ? 0.63F : 0.43F;
        return 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void onCrossbowShot(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack) {
        if (pShooter instanceof ServerPlayer serverplayer) {
            if (!pLevel.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, pCrossbowStack);
            }
            serverplayer.awardStat(Stats.ITEM_USED.get(pCrossbowStack.getItem()));
        }

        clearChargedProjectiles(pCrossbowStack);
    }

    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if (!pLevel.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pStack);
            SoundEvent soundevent = this.getStartSound(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(pStack.getUseDuration() - pCount) / (float)getChargeDuration(pStack);
            if (f < 0.4F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.4F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 1F && soundevent1 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    public int getUseDuration(ItemStack pStack) {
        return MAX_CHARGE_DURATION;
    }

    public static int getChargeDuration(ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pCrossbowStack);
        return MAX_CHARGE_DURATION;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int pEnchantmentLevel) {
        switch (pEnchantmentLevel) {
            case 1 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            }
            case 2 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            }
            case 3 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            }
            default -> {
                return SoundEvents.CROSSBOW_LOADING_START;
            }
        }
    }

    private static float getPowerForTime(int pUseTime, ItemStack pCrossbowStack) {
        float f = (float)pUseTime / (float)getChargeDuration(pCrossbowStack);
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        List<ItemStack> list = getChargedProjectiles(pStack);
        if (isCharged(pStack) && !list.isEmpty()) {
            ItemStack itemstack = list.get(0);
            pTooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(CommonComponents.SPACE).append(itemstack.getDisplayName()));
            if (pFlag.isAdvanced() && itemstack.is(Items.FIREWORK_ROCKET)) {
                List<Component> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, pLevel, list1, pFlag);
                if (!list1.isEmpty()) {
                    list1.replaceAll(component -> Component.literal("  ").append(component).withStyle(ChatFormatting.GRAY));
                    pTooltip.addAll(list1);
                }
            }
        }
        pTooltip.add(Component.translatable("noez.mechanical_crossbow.tooltip1"));
        pTooltip.add(Component.translatable("noez.mechanical_crossbow.tooltip2"));
    }

    public boolean useOnRelease(ItemStack pStack) {
        return pStack.is(this);
    }

    public int getDefaultProjectileRange() {
        return DEFAULT_RANGE;
    }
}

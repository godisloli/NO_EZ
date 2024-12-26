package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezCapacity;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.entity.nonarrows.GrassSpellShot;
import net.tiramisu.noez.item.SpellCaster;
import net.tiramisu.noez.particles.NoezParticles;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DruvisStaff extends SpellCaster {
    private final static int cooldownTicks = 15 * 20;
    private final static int manaCostAttack = 2;
    private final static int manaCostAbility = 4;
    private final static float abilityDamage = 3.5f;
    private final static double magicDamage = 2.5;

    public DruvisStaff(){
        super(
                new Properties().stacksTo(1).durability(134),
                1,
                -2.4f,
                cooldownTicks,
                null,
                magicDamage
        );
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() instanceof DruvisStaff;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("wip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onSwing(Player player, ItemStack stack){
        player.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
            if (!player.getAbilities().instabuild && !(mana.getMana() < manaCostAttack)) {
                mana.consumeMana(manaCostAttack);
                stack.hurt(1, RandomSource.create(), null);
            }
            Level level = player.level();
            if (!level.isClientSide) {
                GrassSpellShot spellShot = new GrassSpellShot(NoezEntities.GRASS_SPELL_SHOT.get(), player, level);
                spellShot.setOwner(player);
                spellShot.setLifespan(20);
                spellShot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,  2F, 1.0F);
                spellShot.setNoGravity(true);
                level.addFreshEntity(spellShot);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CHERRY_LEAVES_BREAK, SoundSource.PLAYERS, 2.0F, 1.0F);
            }
        });
    }

    @Override
    public void onActivate(Player player, ItemStack itemStack, int cooldownTicks){
        player.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
            if (mana.isEmpty())
                return;
            if (!player.getAbilities().instabuild) {
                mana.consumeMana(manaCostAbility);
                itemStack.hurt(1, RandomSource.create(), null);
            }
            if (!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                if (player.level() instanceof ServerLevel serverLevel) {
                    double radius = 3;
                    var entities = serverLevel.getEntitiesOfClass(LivingEntity.class,
                            player.getBoundingBox().inflate(radius),
                            entity -> entity != player
                    );
                    for (LivingEntity entity : entities) {
                        entity.hurt(player.damageSources().magic(), abilityDamage);
                        entity.addEffect(new MobEffectInstance(NoezEffects.ROOT.get(), 4 * 20, 1));
                    }
                    serverLevel.sendParticles(
                            NoezParticles.BLOOD_SLASH.get(),
                            player.getX(),
                            player.getY() + 0.2,
                            player.getZ(),
                            1,
                            0, 0, 0,
                            0
                    );
                }
                player.getCooldowns().addCooldown(this, cooldownTicks);
            }
        });
    }
}

package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostbaneSword extends SwordItem implements Critable {

    private final int FROST_DURATION = 4; //seconds
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private static boolean ALWAYS_CRIT = false;
    private static int COOLDOWN = 12;

    public FrostbaneSword(Tier tier, int Damage, float AttackSpeed, Properties properties) {
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public double getCritChance() {
        return CRIT_CHANCE;
    }

    @Override
    public double getCritDamageAmplifier() {
        return CRIT_DAMAGE;
    }

    @Override
    public boolean isAlwaysCrit(){
        return ALWAYS_CRIT;
    }

    @Override
    public void setAlwaysCrit(boolean value){
        this.ALWAYS_CRIT = value;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.frostbane_sword.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.frostbane_sword.tooltip2"));
        pTooltipComponents.add(Component.translatable("noez.frostbane_sword.tooltip3", COOLDOWN));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide() && !(pStack.getDamageValue() == pStack.getMaxDamage() - 1)) {
            if (pAttacker instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
                player.level().playSound(
                        null,
                        pTarget.getX(),
                        pTarget.getY(),
                        pTarget.getZ(),
                        SoundEvents.GLASS_BREAK,
                        SoundSource.PLAYERS,
                        1f,
                        1f
                );
                pTarget.addEffect(new MobEffectInstance(NoezEffects.FROSTBITE.get(), FROST_DURATION * 20, 1));
                player.getCooldowns().addCooldown(this, COOLDOWN * 20);
            }
            if (pTarget.fireImmune()){
                pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), 3);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}

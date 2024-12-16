package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkDevastation extends AxeItem implements Critable {
    private static final int SHRED_DURATION = 7;
    private static final double CRIT_CHANCE = 0.25;
    private static final double CRIT_DAMAGE = 1.5;
    private boolean ALWAYS_CRIT = false;

    public DarkDevastation(Tier tier, int Damage, float AttackSpeed, Properties properties){
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
        pTooltipComponents.add(Component.translatable("noez.dark_devastation.tooltip1"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide() && !(pStack.getDamageValue() == pStack.getMaxDamage() - 1)) {
            if (pTarget.hasEffect(NoezEffects.CORRUPTION.get())) {
                MobEffectInstance currentEffect = pTarget.getEffect(NoezEffects.CORRUPTION.get());
                int currentAmplifier = currentEffect != null ? currentEffect.getAmplifier() : 0;
                pTarget.addEffect(new MobEffectInstance(NoezEffects.CORRUPTION.get(), SHRED_DURATION * 20,  Math.min(4, currentAmplifier + 1)));
                if (currentAmplifier == 3) {
                    pTarget.level().playSound(
                            null,
                            pTarget.getX(),
                            pTarget.getY(),
                            pTarget.getZ(),
                            NoezSounds.ARMOR_SHRED.get(),
                            SoundSource.NEUTRAL,
                            0.8f,
                            1f
                    );
                }
            } else {
                pTarget.addEffect(new MobEffectInstance(NoezEffects.CORRUPTION.get(), SHRED_DURATION * 20));
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}

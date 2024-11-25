package net.tiramisu.noez.item.advanceditem;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class NaturaBlade extends SwordItem {
    private static final int TickDuration = 20; //20 tick for 1 second
    private static final int Amplifier = 1;
    private static final int TickCooldown = 300;

    public NaturaBlade(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);

        if (attacker instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, TickDuration, Amplifier));
                player.getCooldowns().addCooldown(this, TickCooldown);
            }
        }
        return true;
    }
}

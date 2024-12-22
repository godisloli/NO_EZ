package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezCapacity;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.item.NoezToolTier;
import net.tiramisu.noez.item.ProjectileSword;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImperialiteSword extends ProjectileSword implements Critable {
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private static final float ONHIT_DAMAGE = 4;
    private static final int ONHIT_MANA_CONSUME = 2;
    private boolean ALWAYS_CRIT = false;

    public ImperialiteSword() {
        super(
                new Properties().stacksTo(1).durability(320),
                0,
                null,
                NoezToolTier.MEDIUM,
                1,
                -2.8f
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.imperialite_sword.tooltip1"));
        pTooltipComponents.add(Component.translatable("wip"));
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() instanceof ImperialiteSword;
    }

    @Override
    public void onSwing(Player player, ItemStack stack) {
        Level level = player.level();
        if (!level.isClientSide) {
            player.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
                if (!(mana.getMana() < ONHIT_MANA_CONSUME && !player.getAbilities().instabuild)) {
                    mana.consumeMana(ONHIT_MANA_CONSUME);
                }
            });
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
            if (!(mana.isEmpty())) {
                target.hurt(attacker.damageSources().magic(), ONHIT_DAMAGE);
            }
        });
        return super.hurtEnemy(stack, target, attacker);
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
}

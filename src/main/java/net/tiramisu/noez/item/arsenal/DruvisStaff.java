package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezCapacity;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.entity.nonarrows.GrassSpellShot;
import net.tiramisu.noez.item.SpellCaster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DruvisStaff extends SpellCaster {
    private final static int cooldownTicks = 15 * 20;
    private final static int manaCostAttack = 1;
    private final static double magicDamage = 2;

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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("wip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onSwing(Player player, ItemStack stack){
        if (player.getAbilities().instabuild)
            return;
        player.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
            if (mana.isEmpty())
                return;
            mana.consumeMana(manaCostAttack);
            stack.hurt(1, RandomSource.create(), null);
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

    }
}

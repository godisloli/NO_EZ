package net.tiramisu.noez.item.consumables;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.ManaPlayer;

import javax.annotation.Nullable;
import java.util.List;

public class ManaPotion extends PotionItem {
    public ManaPotion(Properties properties){
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag){
        tooltip.add(Component.translatable("item.noez.mana_potion.tooltip"));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (entity instanceof ServerPlayer player)
            if (!player.getAbilities().instabuild) {
                player.getCapability(ManaPlayer.MANA).ifPresent(mana -> {
                    mana.addMana(5);
                });
                stack.shrink(1);
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
        return stack;
    }
}

package net.tiramisu.noez.item.consumables;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;

import javax.annotation.Nullable;
import java.util.List;

public class ManaPotion extends PotionItem {
    public ManaPotion(Properties properties){
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag){
        tooltip.add(Component.translatable("item.noez.mana_potion.tooltip").withStyle(net.minecraft.ChatFormatting.BLUE));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide) {
            entity.addEffect(new MobEffectInstance(NoezEffects.INSTANT_MANA.get(), 1, 0));
        }
        if (entity instanceof Player player)
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
        }
        return stack;
    }
}

package net.tiramisu.noez.item.weaponstools;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class IridescentBow extends BowItem {
    public IridescentBow(Item.Properties properties){
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}

package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.npc.VillagerTrades$EnchantedItemForEmeralds")
public abstract class NoEnchantedItem implements VillagerTrades.ItemListing {

    @Inject(method = "getOffer", at = @At("HEAD"), cancellable = true)
    private void removeEnchantedItemTrades(CallbackInfoReturnable<MerchantOffer> cir) {
        cir.setReturnValue(null);
        System.out.println("canceled");
    }
}


package net.tiramisu.noez.mixin;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.npc.VillagerTrades$EmeraldForItems")
public abstract class VillagerTradesMixin implements ItemListing {

    @Inject(method = "getOffer", at = @At("RETURN"), cancellable = true)
    private void removeEnchantedItemTrades(CallbackInfoReturnable<MerchantOffer> cir) {
        MerchantOffer offer = cir.getReturnValue();
        if (offer != null && !EnchantmentHelper.getEnchantments(offer.getResult()).isEmpty()) {
            cir.setReturnValue(null);
        }
    }
}

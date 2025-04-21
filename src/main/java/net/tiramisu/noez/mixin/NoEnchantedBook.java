package net.tiramisu.noez.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(targets = "net.minecraft.world.entity.npc.VillagerTrades$EnchantBookForEmeralds")
public abstract class NoEnchantedBook implements VillagerTrades.ItemListing {

    @Inject(method = "getOffer", at = @At("HEAD"), cancellable = true)
    private void cancelEnchantedBookOffers(CallbackInfoReturnable<MerchantOffer> cir) {
            cir.setReturnValue(null);
            System.out.println("[Mixin] Canceled enchanted book trade");
    }
}

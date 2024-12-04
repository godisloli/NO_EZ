package net.tiramisu.noez.event.global;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class NoMiningToolBreak {
    private static final Map<Player, Long> lastSoundTimeMap = new HashMap<>();
    private static final long SOUND_DELAY_MS = 1; // Delay in milliseconds

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            long currentTime = System.currentTimeMillis();
            long lastSoundTime = lastSoundTimeMap.getOrDefault(player, 0L);
            if (currentTime - lastSoundTime >= SOUND_DELAY_MS) {
                if (!player.level().isClientSide) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                lastSoundTimeMap.put(player, currentTime);
            }
            event.setCanceled(true);
        }
    }
}

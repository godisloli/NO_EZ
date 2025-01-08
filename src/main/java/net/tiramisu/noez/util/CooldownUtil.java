package net.tiramisu.noez.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class CooldownUtil {
    private static final String COOLDOWN_DATA_KEY = "Cooldowns";
    private static Field cooldownsField;
    private static Field startTimeField;
    private static Field endTimeField;
    static {
        try {
            cooldownsField = ItemCooldowns.class.getDeclaredField("cooldowns");
            cooldownsField.setAccessible(true);
            Class<?> cooldownInstanceClass = Class.forName("net.minecraft.world.item.ItemCooldowns$CooldownInstance");
            startTimeField = cooldownInstanceClass.getDeclaredField("startTime");
            startTimeField.setAccessible(true);
            endTimeField = cooldownInstanceClass.getDeclaredField("endTime");
            endTimeField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveCooldowns(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag cooldownData = new CompoundTag();

        try {
            Map<Item, ?> cooldowns = getCooldowns(player.getCooldowns());
            if (cooldowns != null) {
                for (Map.Entry<Item, ?> entry : cooldowns.entrySet()) {
                    Item item = entry.getKey();
                    Object cooldownInstance = entry.getValue();
                    int startTime = (int) startTimeField.get(cooldownInstance);
                    int endTime = (int) endTimeField.get(cooldownInstance);
                    int remainingTicks = (endTime - startTime);
                    if (remainingTicks > 0) {
                        cooldownData.putInt(Item.getId(item) + "", remainingTicks);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        playerData.put(COOLDOWN_DATA_KEY, cooldownData);
    }

    public static void restoreCooldowns(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        if (playerData.contains(COOLDOWN_DATA_KEY)) {
            CompoundTag cooldownData = playerData.getCompound(COOLDOWN_DATA_KEY);

            for (String key : cooldownData.getAllKeys()) {
                int ticks = cooldownData.getInt(key);
                Item item = Item.byId(Integer.parseInt(key));
                player.getCooldowns().addCooldown(item, ticks);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Item, ?> getCooldowns(ItemCooldowns cooldowns) throws IllegalAccessException {
        if (cooldownsField != null) {
            return (Map<Item, ?>) cooldownsField.get(cooldowns);
        }
        return new HashMap<>();
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal() instanceof ServerPlayer original && event.getEntity() instanceof ServerPlayer clone) {
            CooldownUtil.saveCooldowns(original);
            clone.getServer().execute(() -> CooldownUtil.restoreCooldowns(clone));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CooldownUtil.saveCooldowns(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CooldownUtil.restoreCooldowns(player);
        }
    }
}

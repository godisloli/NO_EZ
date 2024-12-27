package net.tiramisu.noez.event.global;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Map;

@Mod.EventBusSubscriber
public class HungerDrain {
    private static final Map<Player, Integer> playerTimers = new java.util.HashMap<>();
    private static final int EASY_INTERVAL = 60 * 20;
    private static final int NORMAL_INTERVAL = 50 * 20;
    private static final int HARD_INTERVAL = 40 * 20;

    @SubscribeEvent
    public static void drainHungerPoint(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide() || event.player.getAbilities().instabuild) return;
        Player player = event.player;
        if (player instanceof ServerPlayer) {
            FoodData foodData = player.getFoodData();
            playerTimers.putIfAbsent(player, 0);
            int timer = playerTimers.get(player) + 1;
            playerTimers.put(player, timer);
            Difficulty difficulty = player.level().getDifficulty();
            int interval = getIntervalForDifficulty(difficulty);
            if (interval > 999)
                return;
            if (timer >= interval) {
                foodData.setFoodLevel(Math.max(foodData.getFoodLevel() - 1, 0));
                playerTimers.put(player, 0);
            }
        }
    }

    private static int getIntervalForDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> EASY_INTERVAL;
            case NORMAL -> NORMAL_INTERVAL;
            case HARD -> HARD_INTERVAL;
            default -> 99999;
        };
    }
}

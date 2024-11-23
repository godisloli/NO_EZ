package net.tiramisu.noez.event.serverstarting;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.server.ServerStartingEvent;

public class Starter {
    public void DisableNaturalRegeneration(ServerStartingEvent event,boolean condition){
        if (condition)
        {
            GameRules gameRules = event.getServer().getGameRules();
            GameRules.BooleanValue naturalRegenRule = gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION);
            naturalRegenRule.set(false, event.getServer());
        }
    }
}

package net.tiramisu.noez.event.serverstarting;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.server.ServerStartingEvent;


public class Starter {
    public void onServerStart(ServerStartingEvent event, boolean condition){
        if (condition)
        {
            GameRules gameRules = event.getServer().getGameRules();
            GameRules.BooleanValue naturalRegenRule = gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION);
            naturalRegenRule.set(false, event.getServer());
            GameRules.BooleanValue keepInventoryRule = gameRules.getRule(GameRules.RULE_KEEPINVENTORY);
            keepInventoryRule.set(true, event.getServer());
        }
    }
}

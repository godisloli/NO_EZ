package net.tiramisu.noez.effect;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.player.LocalPlayer;

public class Stun extends MobEffect {
    public Stun(){
        super(MobEffectCategory.HARMFUL,0x4B0082);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Check if it's the player and they have the stun effect
        if (entity instanceof LocalPlayer player) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen != null) {
                return;
            }
            disablePlayerInput(player);
        }
    }

    private void disablePlayerInput(LocalPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        mc.options.keyAttack.setDown(false);
        mc.options.keyUse.setDown(false);
        mc.options.keyUp.setDown(false);
        mc.options.keyDown.setDown(false);
        mc.options.keyLeft.setDown(false);
        mc.options.keyRight.setDown(false);
        mc.options.keyJump.setDown(false);
        mc.options.keyShift.setDown(false);
        mc.options.keySprint.setDown(false);
        mc.options.keyPickItem.setDown(false);
        KeyMapping.set(mc.options.keyAttack.getKey(), false);
        KeyMapping.set(mc.options.keyUse.getKey(), false);
        KeyMapping.set(mc.options.keySwapOffhand.getKey(), false);
        KeyMapping.set(mc.options.keyDrop.getKey(), false);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 1 == 0;
    }
}

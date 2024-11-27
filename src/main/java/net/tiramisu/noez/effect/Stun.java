package net.tiramisu.noez.effect;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class Stun extends MobEffect {
    public Stun(){
        super(MobEffectCategory.HARMFUL,0x4B0082);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {

        Minecraft minecraft = Minecraft.getInstance();
        KeyMapping forwardKey = minecraft.options.keyUp;
        KeyMapping backKey = minecraft.options.keyDown;
        KeyMapping leftKey = minecraft.options.keyLeft;
        KeyMapping rightKey = minecraft.options.keyRight;
        KeyMapping jumpKey = minecraft.options.keyJump;
        KeyMapping sneakKey = minecraft.options.keyShift;

        forwardKey.setDown(false);
        backKey.setDown(false);
        leftKey.setDown(false);
        rightKey.setDown(false);
        jumpKey.setDown(false);
        sneakKey.setDown(false);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}

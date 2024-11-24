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
}

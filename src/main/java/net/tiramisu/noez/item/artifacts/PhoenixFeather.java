package net.tiramisu.noez.item.artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.ArtifactItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PhoenixFeather extends ArtifactItem {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("5d1f7e0c-2df7-4b3a-9d5e-77b045a689bd");
    private static final float ATK_MODIFIER = 1.5f;

    public PhoenixFeather(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.phoenix_feather.tooltip1"));
    }

    @Override
    public void applyEffect(ServerPlayer player) {
        AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr == null) return;

        if (player.isOnFire()) {
            if (attackAttr.getModifier(ATTACK_DAMAGE_UUID) == null) {
                AttributeModifier modifier = new AttributeModifier(
                        ATTACK_DAMAGE_UUID,
                        "Phoenix Feather Bonus",
                        ATK_MODIFIER,
                        AttributeModifier.Operation.ADDITION
                );
                attackAttr.addTransientModifier(modifier);
            }
        } else {
            if (attackAttr.getModifier(ATTACK_DAMAGE_UUID) != null) {
                attackAttr.removeModifier(ATTACK_DAMAGE_UUID);
            }
        }
    }

    @Override
    public void removeEffect(ServerPlayer player) {
        AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackAttr != null && attackAttr.getModifier(ATTACK_DAMAGE_UUID) != null) {
            attackAttr.removeModifier(ATTACK_DAMAGE_UUID);
        }
    }

    @Override
    public void playSound(Level level, ServerPlayer player) {
        level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f, 1f);
    }
}
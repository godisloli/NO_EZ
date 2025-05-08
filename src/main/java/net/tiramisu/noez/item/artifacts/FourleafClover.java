package net.tiramisu.noez.item.artifacts;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArtifactItem;

import java.util.UUID;

public class FourleafClover extends ArtifactItem {
    private static final UUID CRIT_CHANCE_UUID = UUID.fromString("4d1f7e0c-2df6-4b3a-9d5e-77b045a689bc");
    private static final float CRIT_MODIFIER = 15f;
    
    public FourleafClover(Properties properties) {
        super(properties);
    }
    
    @Override
    public void applyEffect(ServerPlayer player) {
        AttributeInstance critAttr = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
        if (critAttr == null) return;

        if (critAttr.getModifier(CRIT_CHANCE_UUID) == null) {
            AttributeModifier modifier = new AttributeModifier(
                    CRIT_CHANCE_UUID,
                    "Phoenix Feather Bonus",
                    CRIT_MODIFIER,
                    AttributeModifier.Operation.ADDITION
            );
            critAttr.addTransientModifier(modifier);
        }
    }
    
    @Override
    public void removeEffect(ServerPlayer player) {
        AttributeInstance critAttr = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
        if (critAttr != null && critAttr.getModifier(CRIT_CHANCE_UUID) != null) {
            critAttr.removeModifier(CRIT_CHANCE_UUID);
        }
    }

    @Override
    public void playSound(Level level, ServerPlayer player) {
        level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1f, 1f);
    }
}

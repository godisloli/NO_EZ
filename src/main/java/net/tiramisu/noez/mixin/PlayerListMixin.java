package net.tiramisu.noez.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3;
import net.tiramisu.noez.util.HealthFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    public PlayerListMixin() {
    }

    @Inject(
            method = {"respawn(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;setHealth(F)V"
            )},
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onPlayerRespawn(ServerPlayer oldPlayer, boolean fromEnd, CallbackInfoReturnable<ServerPlayer> cbi, BlockPos respawnPos, float respawnAngle, boolean wasForced, ServerLevel oldDimension, Optional<Vec3> calculatedPos, ServerLevel overworld, ServerPlayer newPlayer) {
        if (newPlayer instanceof HealthFix fixable) {
            fixable.maxhealthfix$setRestorePoint(oldPlayer.getHealth());
        }
    }
}

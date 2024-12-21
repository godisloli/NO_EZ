package net.tiramisu.noez.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.network.packet.PlayerMovementC2SPacket;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = NOEZ.MOD_ID, value = Dist.CLIENT)
public class PlayerInputHandler {
    private static final Minecraft MC = Minecraft.getInstance();
    private static final Set<String> pressedKeys = new HashSet<>();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(PlayerInputHandler.class);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (MC.player == null) return;
        boolean isPressingForward = MC.options.keyUp.isDown();
        boolean isPressingBack = MC.options.keyDown.isDown();
        boolean isPressingLeft = MC.options.keyLeft.isDown();
        boolean isPressingRight = MC.options.keyRight.isDown();
        boolean isJumping = MC.options.keyJump.isDown();
        pressedKeys.clear();
        if (isPressingForward) pressedKeys.add("W");
        if (isPressingBack) pressedKeys.add("S");
        if (isPressingLeft) pressedKeys.add("A");
        if (isPressingRight) pressedKeys.add("D");
        if (isJumping) pressedKeys.add("SPACE");

        if (!pressedKeys.isEmpty()) {
            NoezNetwork.senDatatoServer(new PlayerMovementC2SPacket(pressedKeys));
        }
    }
}

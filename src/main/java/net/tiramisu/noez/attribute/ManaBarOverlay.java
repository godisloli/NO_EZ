package net.tiramisu.noez.attribute;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaBarOverlay {
    private static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/gui/mana_bar.png");
    private static final ResourceLocation MANA_POINT_TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/gui/mana_point.png");
    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null) {
            player.getCapability(NoezAttribute.MANA).ifPresent(mana -> {
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int x = event.getWindow().getGuiScaledWidth() / 2 - 91;
                int y = event.getWindow().getGuiScaledHeight() - 49;
                RenderSystem.setShaderTexture(0, MANA_BAR_TEXTURE);
                for (int i = 0; i < 10; i++) {
                    guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 0, 0, 9, 9); // Background
                }
                int filledMana = (int) Math.ceil((mana.getMana() / (float) mana.getMaxMana()) * 10);
                for (int i = 0; i < filledMana; i++) {
                    guiGraphics.blit(MANA_POINT_TEXTURE, x + i * 8, y, 9, 0, 9, 9); // Foreground
                }
            });
        }
    }
}


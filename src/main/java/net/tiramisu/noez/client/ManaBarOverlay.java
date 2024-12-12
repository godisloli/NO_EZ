package net.tiramisu.noez.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.attribute.NoezAttribute;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaBarOverlay {
    private static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/gui/mana_bar.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player.isCreative())
            return;
        if (player != null) {
            player.getCapability(NoezAttribute.MANA).ifPresent(mana -> {
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int x = event.getWindow().getGuiScaledWidth() / 2 + 8;
                int y = event.getWindow().getGuiScaledHeight() - 49;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                for (int i = 0; i < 10; i++) {
                    guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 0, 0, 8, 8);
                }
                int filledMana = (int) Math.ceil((mana.getMana() / (float) mana.getMaxMana()) * 10);
                if (filledMana % 2 == 0) {
                    for (int i = 0; i < filledMana; i++) {
                        guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 10, 0, 8, 8);
                    }
                } else {
                    for (int i = 0; i < filledMana; i++) {
                        guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 12, 0, 8, 8);
                    }
                }
            });
        }
    }
}

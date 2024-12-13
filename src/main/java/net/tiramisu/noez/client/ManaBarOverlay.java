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
import net.tiramisu.noez.attribute.NoezCapacity;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaBarOverlay {
    private static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/gui/mana_bar.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player.isCreative())
            return;
        player.getCapability(NoezCapacity.MANA).ifPresent(mana -> {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int x = event.getWindow().getGuiScaledWidth() / 2 + 9;
            int y = event.getWindow().getGuiScaledHeight() - 49;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (int i = 0; i < 10; i++) {
                guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 0, 0, 9, 9);
            }
            for (int i = 0; i < 10; i++) {
                if (ClientManaData.getMana() / 2 > i) {
                    guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 10, 0, 9, 9);
                } else if (ClientManaData.getMana() % 2 != 0 && ClientManaData.getMana() / 2 == i) {
                    guiGraphics.blit(MANA_BAR_TEXTURE, x + i * 8, y, 20, 0, 9, 9);
                } else {
                    break;
                }
            }

        });
    }
}

package net.tiramisu.noez.client;

import net.minecraft.world.item.ItemStack;

public class ArtifactData {
    private static ItemStack currentArtifact = ItemStack.EMPTY;

    public static void handleSync(ItemStack stack) {
        currentArtifact = stack;
    }

    public static ItemStack getCurrentArtifact() {
        return currentArtifact;
    }
}

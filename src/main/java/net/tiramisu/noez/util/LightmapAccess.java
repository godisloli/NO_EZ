package net.tiramisu.noez.util;

public interface LightmapAccess {
    boolean darkness_isDirty();
    float darkness_prevFlicker();
}

package net.tiramisu.noez.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NoezSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "noez");

    public static final RegistryObject<SoundEvent> WINDBREAKER_HIT = registerSoundEvent("windbreaker_hit");
    public static final RegistryObject<SoundEvent> WINDBREAKER_SLAM = registerSoundEvent("windbreaker_slam");
    public static final RegistryObject<SoundEvent> ARMOR_SHRED = registerSoundEvent("armor_shred");
    public static final RegistryObject<SoundEvent> ECHO_ELYTRA_UPDRAFT = registerSoundEvent("echo_elytra_updraft");
    public static final RegistryObject<SoundEvent> KITSUNE_PROC = registerSoundEvent("kitsune");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("noez", name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

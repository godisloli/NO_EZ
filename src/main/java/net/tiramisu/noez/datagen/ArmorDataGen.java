package net.tiramisu.noez.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;

@Mod.EventBusSubscriber(modid = NOEZ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorDataGen {

    public static class ArmorLangGen extends LanguageProvider {
        public ArmorLangGen(PackOutput output, String locale) {
            super(output, NOEZ.MOD_ID, locale);
        }

        @Override
        protected void addTranslations() {
            addArmorSet("infiltrator_helmet", "Infiltrator Helmet");
            addArmorSet("infiltrator_chestplate", "Infiltrator Chestplate");
            addArmorSet("infiltrator_leggings", "Infiltrator Leggings");
            addArmorSet("infiltrator_boots", "Infiltrator Boots");
        }

        private void addArmorSet(String key, String name) {
            add("item." + NOEZ.MOD_ID + "." + key, name);
        }
    }

    public static class ArmorModelGen extends ItemModelProvider {
        public ArmorModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, NOEZ.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            armorModel("infiltrator_helmet");
            armorModel("infiltrator_chestplate");
            armorModel("infiltrator_leggings");
            armorModel("infiltrator_boots");
        }

        private void armorModel(String name) {
            getBuilder(name)
                    .parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", NOEZ.MOD_ID + ":item/" + name);
        }
    }

    @SubscribeEvent
    public static void register(GatherDataEvent event) {
        if (event.includeClient()) {
            PackOutput packOutput = event.getGenerator().getPackOutput();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            event.getGenerator().addProvider(true, new ArmorLangGen(packOutput, "en_us"));
            event.getGenerator().addProvider(true, new ArmorModelGen(packOutput, existingFileHelper));
        }
    }
}

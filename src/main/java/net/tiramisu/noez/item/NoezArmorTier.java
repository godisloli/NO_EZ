package net.tiramisu.noez.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.tiramisu.noez.NOEZ;

import java.util.function.Supplier;

public enum NoezArmorTier implements ArmorMaterial {
    ECHO("echo", 42, new int[]{5, 10, 9, 4}, 0,
            SoundEvents.SCULK_CATALYST_PLACE, 5.0F, 0.2F, () -> Ingredient.of(NoezItems.WARDEN_HEART.get())),

    ROYAL_GUARD("royal_guard", 36, new int[]{4, 9, 8, 3}, 0,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.3F, () -> Ingredient.EMPTY),

    GUARDIAN_ANGEL("guardian_angel", 32, new int[]{4, 9, 7, 4}, 0,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> Ingredient.EMPTY),

    TWILIGHT_WITCH_HAT("twilight_witch_hat",22, new int[]{1, 0, 0, 0}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, () -> Ingredient.EMPTY),

    MASTERCRAFT_IRON("mastercraft_iron", 26, new int[]{3, 7, 6, 3}, 0,
            SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0, () -> Ingredient.of(Items.IRON_INGOT)),

    MASTERCRAFT_GOLD("mastercraft_gold", 23, new int[]{2, 8, 5, 4}, 0,
            SoundEvents.ARMOR_EQUIP_GOLD, 2.0F, 0, () -> Ingredient.of(Items.GOLD_INGOT)),

    MASTERCRAFT_DIAMOND("mastercraft_diamond", 29, new int[]{4, 9, 7, 4}, 0,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0F, 0.2F, () -> Ingredient.of(Items.DIAMOND)),

    MASTERCRAFT_NETHERITE("master_netherite", 32, new int[]{5, 10 ,8, 5}, 0,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.5F, () -> Ingredient.of(Items.NETHERITE_INGOT)),

    SYLVAN("sylvan", 41, new int[]{3, 8, 6, 3}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 4.0F, 0.5F, () -> Ingredient.EMPTY),

    CACTUS("cactus", 16, new int[]{1, 3, 2, 1}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, () -> Ingredient.of(Items.CACTUS)),

    DRUID("druid", 22, new int[]{2, 3, 2, 1}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0, () -> Ingredient.EMPTY),

    KITSUNE("kitsune", 44, new int[]{2, 1, 1, 1}, 0,
            SoundEvents.ALLAY_AMBIENT_WITH_ITEM, 2.0F, 0.0f, () -> Ingredient.EMPTY),

    EXPLORER("explorer", 27, new int[]{2, 4, 3, 2}, 0,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0F, 0F, () -> Ingredient.of(Items.LEATHER)),

    END_WALKER("end_walker", 48, new int[]{3, 5, 4, 2}, 0,
            SoundEvents.ENDERMAN_AMBIENT, 4.0F, 0.5F, () -> Ingredient.of(Items.ENDER_PEARL));

    private static final int[] BASE_DURABILITY = {13, 18, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] defensePoints;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    NoezArmorTier(String name, int durabilityMultiplier, int[] defensePoints, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.defensePoints = defensePoints;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.defensePoints[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return NOEZ.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}


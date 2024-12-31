package net.tiramisu.noez.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.tiramisu.noez.util.NoezTags;

public enum NoezToolTier implements Tier{
    LIGHT(1, 89, 12.0F, 2.0F, 22, Ingredient.of(NoezTags.Items.LIGHT_WEAPON_REPAIRABLE)),
    MEDIUM(2,125, 10.0F, 3F, 22, Ingredient.of(NoezTags.Items.MEDIUM_WEAPON_REPAIRABLE)),
    HEAVY(4,172, 10.0F, 5F, 22, Ingredient.of(NoezTags.Items.HEAVY_WEAPON_REPAIRABLE));

    private final int harvestLevel;
    // 0: Can mine basic blocks (wood, coal ore).
    // 1: Can mine stone-tier blocks (iron ore).
    // 2: Can mine iron-tier blocks (diamond ore, gold ore).
    // 3: Can mine diamond-tier blocks (obsidian).
    // 4+: Custom tiers for mods; typically used for stronger or custom blocks.

    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;

    NoezToolTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Ingredient repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(NoezTags.Items.LIGHT_WEAPON_REPAIRABLE);
    }
}

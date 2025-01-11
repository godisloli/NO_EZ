package net.tiramisu.noez.item.armors;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.tiramisu.noez.item.ArmorAttribute;

public class RoyalGuardArmor extends ArmorItem implements ArmorAttribute {
    public RoyalGuardArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public String getTooltipID() {
        return "";
    }

    @Override
    public void setTooltipID(String tooltipID) {

    }

    @Override
    public String helmetTooltip() {
        return "";
    }

    @Override
    public String chesplateTooltip() {
        return "";
    }

    @Override
    public String leggingsTooltip() {
        return "";
    }

    @Override
    public String bootsTooltip() {
        return "";
    }

    @Override
    public float helmetValue() {
        return 0;
    }

    @Override
    public float chesplateValue() {
        return 0;
    }

    @Override
    public float leggingsValue() {
        return 0;
    }

    @Override
    public float bootsValue() {
        return 0;
    }
}

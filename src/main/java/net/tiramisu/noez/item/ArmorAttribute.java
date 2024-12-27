package net.tiramisu.noez.item;

public interface ArmorAttribute {
    String getTooltipID();
    void setTooltipID(String tooltipID);
    String helmetTooltip();
    String chesplateTooltip();
    String leggingsTooltip();
    String bootsTooltip();
    float helmetValue();
    float chesplateValue();
    float leggingsValue();
    float bootsValue();
}

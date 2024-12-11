package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class ArmorCrunch extends MobEffect {
    public ArmorCrunch(){
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
//        if (!entity.level().isClientSide) {
//            double reducedArmor = 0;
//            int reductionPoints;
//            switch (amplifier) {
//                case 0 -> reductionPoints = 1;
//                case 1 -> reductionPoints = 2;
//                case 2 -> reductionPoints = 3;
//                case 3 -> reductionPoints = 4;
//                case 4 -> reductionPoints = 5;
//                default -> reductionPoints = 6;
//            }
//            for (EquipmentSlot slot : EquipmentSlot.values()) {
//                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
//                    ItemStack armorPiece = entity.getItemBySlot(slot);
//                    if (armorPiece.getItem() instanceof ArmorItem armorItem) {
//                        int originalArmor = armorItem.getDefense();
//                        int reduction = Math.min(originalArmor, reductionPoints);
//                        reducedArmor += originalArmor - reduction;
//                    }
//                }
//            }
//            entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR)
//                    .setBaseValue(reducedArmor);
//        }
//        ============WORK IN PROGRESS============
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0 && duration % 20 == 0;
    }
}

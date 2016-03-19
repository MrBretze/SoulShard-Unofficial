package fr.bretzel.soulshard.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class SoulStealer extends Enchantment {

    public SoulStealer(Rarity rarity, EnumEnchantmentType enumEnchantmentType, EntityEquipmentSlot[] entityEquipmentSlots) {
        super(rarity, enumEnchantmentType, entityEquipmentSlots);
        setName("soul_shard.soul_stealer");
    }

    @Override
    public int getMinEnchantability(int i) {
        return (i - 1) * 11;
    }

    @Override
    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}

package fr.bretzel.soulshard.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class SoulStealer extends Enchantment {

    public SoulStealer(int id, ResourceLocation location, int max, EnumEnchantmentType type) {
        super(id, location, max, type);
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

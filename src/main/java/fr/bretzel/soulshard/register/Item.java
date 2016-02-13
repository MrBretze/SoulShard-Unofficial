package fr.bretzel.soulshard.register;

import fr.bretzel.soulshard.Reference;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.enchantment.SoulStealer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class Item {

    public static SoulStealer soulStealer;

    public static void registerItem() {

    }

    public static void registerEnchantems() {
        int soulID = Common.soulConfig.soulStealerID;

        if (Enchantment.getEnchantmentById(soulID) != null) {
            soulID = getEmptyEnchantId();
            SoulShard.soulLog.warn("The Soul Stealer enchantment is already registered, a new id is set ! (NEW ID: " + soulID + ").");
        }

        soulStealer = new SoulStealer(soulID, new ResourceLocation(Reference.MODID + ".soul_stealer"), Common.soulConfig.soulStealerWeight, EnumEnchantmentType.WEAPON);
        Enchantment.addToBookList(soulStealer);
    }

    private static int getEmptyEnchantId() {
        for (int i = 0; i < 256; i++) {
            if(Enchantment.getEnchantmentById(i) == null) {
                return i;
            }
        }
        return -1;
    }
}

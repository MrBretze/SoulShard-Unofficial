package fr.bretzel.soulshard.registry;

import fr.bretzel.soulshard.Reference;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.enchantment.SoulStealer;
import fr.bretzel.soulshard.item.SoulShardItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {

    public static SoulStealer soulStealer;
    public static SoulShardItem soulShard;

    public static void registerItem() {
        GameRegistry.registerItem(soulShard = new SoulShardItem("soul_shard"), "soul_shard");
    }

    public static void registerEnchantems() {
        int soulID = CommonRegistry.soulConfig.soulStealerID;

        if (Enchantment.getEnchantmentById(soulID) != null) {
            soulID = getEmptyEnchantId();
            SoulShard.soulLog.warn("The Soul Stealer enchantment is already registered, a new id is set ! (NEW ID: " + soulID + ").");
        }

        Enchantment.addToBookList(soulStealer = new SoulStealer(soulID, new ResourceLocation(Reference.MODID + ".soul_stealer"), CommonRegistry.soulConfig.soulStealerWeight, EnumEnchantmentType.WEAPON));
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

package fr.bretzel.soulshard.registry;

import fr.bretzel.soulshard.Reference;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.config.SoulShardConfig;
import fr.bretzel.soulshard.enchantment.SoulStealer;
import fr.bretzel.soulshard.item.SoulShardItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {

    public static SoulStealer soulStealer;
    public static SoulShardItem soulShard;

    public static void registerItem() {
        GameRegistry.registerItem(soulShard = new SoulShardItem("soul_shard"), "soul_shard");
    }

    public static void registerEnchantems() {
        int soulID = SoulShardConfig.soulStealerID;

        if (Enchantment.getEnchantmentByID(soulID) != null) {
            soulID = getEmptyEnchantId();
            SoulShard.soulLog.warn("The Soul Stealer enchantment is already registered, a new id is set ! (NEW ID: " + soulID + ").");
        }

        soulStealer = new SoulStealer(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.CHEST, EntityEquipmentSlot.MAINHAND});

        Enchantment.enchantmentRegistry.register(soulID, new ResourceLocation(Reference.MODID + ".soul_stealer"), soulStealer);
    }

    private static int getEmptyEnchantId() {
        for (int i = 0; i < 256; i++) {
            if(Enchantment.getEnchantmentByID(i) == null) {
                return i;
            }
        }
        return -1;
    }
}

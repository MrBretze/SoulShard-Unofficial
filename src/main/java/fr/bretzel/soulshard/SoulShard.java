package fr.bretzel.soulshard;

import fr.bretzel.soulshard.config.SoulShardConfig;

import fr.bretzel.soulshard.enchantment.SoulStealer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MINECRAFT_VERSION)
public class SoulShard {

    @Mod.Instance
    public static SoulShard instance;

    public static Logger soulLog = FMLLog.getLogger();

    public static SoulShardConfig soulConfig;

    public static SoulStealer soulStealer;

    public static SoulCreativeTab creativeTab;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        String configDirectory = event.getModConfigurationDirectory() + File.separator + Reference.NAME + File.separator;

        soulConfig = new SoulShardConfig(new File(configDirectory, "Main.cfg"));

        creativeTab = new SoulCreativeTab();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        int soulID = soulConfig.soulStealerID;

        if (Enchantment.getEnchantmentById(soulID) != null) {
            soulID = getEmptyEnchantId();
            soulLog.warn("The Soul Stealer enchantment is already registered, a new id is set ! (NEW ID: " + soulID + ").");
        }

        this.soulStealer = new SoulStealer(soulID, new ResourceLocation(Reference.MODID + ".soul_stealer"), soulConfig.soulStealerWeight, EnumEnchantmentType.WEAPON);
        Enchantment.addToBookList(soulStealer);
    }

    private int getEmptyEnchantId() {
        for (int i = 0; i < 256; i++) {
            if(Enchantment.getEnchantmentById(i) == null) {
                return i;
            }
        }
        return -1;
    }
}

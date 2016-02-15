package fr.bretzel.soulshard.registry;

import fr.bretzel.soulshard.Reference;
import fr.bretzel.soulshard.SoulCreativeTab;
import fr.bretzel.soulshard.config.SoulShardConfig;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonRegistry {

    public static SoulCreativeTab creativeTab;
    public static SoulShardConfig soulConfig;

    public static String configDirectory;

    public static void register() {
        creativeTab = new SoulCreativeTab();
    }

    public static void registerConfig(FMLPreInitializationEvent event) {
        configDirectory = event.getModConfigurationDirectory() + File.separator + Reference.NAME + File.separator;
        soulConfig = new SoulShardConfig(new File(configDirectory, "Main.cfg"));
        soulConfig.load();
    }

}

package fr.bretzel.soulshard.proxy;

import fr.bretzel.soulshard.MobMapping;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.registry.BlockRegistry;
import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

public class CommonProxy {

    private String configDirectory;

    public void preInit(FMLPreInitializationEvent e) {
        CommonRegistry.registerConfig(e);
        CommonRegistry.register();

        ItemRegistry.registerEnchantems();
        ItemRegistry.registerItem();
        BlockRegistry.registerBlock();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {

    }

    public void loadWorld(FMLServerStartingEvent event) {
        SoulShard.mobMapping = new MobMapping(event.getServer().getEntityWorld());
    }
}

package fr.bretzel.soulshard.proxy;

import fr.bretzel.soulshard.MobMapping;
import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.event.CommonEvent;
import fr.bretzel.soulshard.registry.BlockRegistry;
import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.registry.CraftingRegistry;
import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        CommonRegistry.registerConfig(e);
        CommonRegistry.register();

        BlockRegistry.registerBlock();
        ItemRegistry.registerEnchantems();
        ItemRegistry.registerItem();
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new CommonEvent());
    }

    public void postInit(FMLPostInitializationEvent e) {
        CraftingRegistry.registerCraft();
    }

    public void loadWorld(FMLServerStartingEvent event) {
        SoulShard.mobMapping = getMapping(event.getServer().getEntityWorld());
    }

    public MobMapping getMapping(World world) {
        if (!MobMapping.isLoaded()) {
            return new MobMapping(world);
        }
        return SoulShard.mobMapping;
    }
}

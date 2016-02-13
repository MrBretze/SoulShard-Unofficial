package fr.bretzel.soulshard.proxy;

import fr.bretzel.soulshard.register.Block;
import fr.bretzel.soulshard.register.Common;
import fr.bretzel.soulshard.register.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        Common.registerConfig(e);
        Common.register();

        Item.registerEnchantems();
        Item.registerItem();
        Block.registerBlock();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}

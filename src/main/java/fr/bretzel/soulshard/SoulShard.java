package fr.bretzel.soulshard;

import fr.bretzel.soulshard.proxy.CommonProxy;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;


@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MINECRAFT_VERSION)
public class SoulShard {

    @Mod.Instance
    public static SoulShard instance;

    public static Logger soulLog = FMLLog.getLogger();
    public static MobMapping mobMapping;
    public static final boolean debug = true;

    @SidedProxy(clientSide = "fr.bretzel.soulshard.proxy.ClientProxy", serverSide = "fr.bretzel.soulshard.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postIni(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadWorld(FMLServerStartingEvent event) {
        proxy.loadWorld(event);
    }
}

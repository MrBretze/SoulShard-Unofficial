package fr.bretzel.soulshard.proxy;

import fr.bretzel.soulshard.event.ClientEvent;
import fr.bretzel.soulshard.registry.RenderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{

	@Override public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		RenderRegistry.registerRender();
	}

	@Override public void init(FMLInitializationEvent e)
	{
		super.init(e);
		MinecraftForge.EVENT_BUS.register(new ClientEvent());
	}

	@Override public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
	}
}

package fr.bretzel.soulshard.registry;

import fr.bretzel.soulshard.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class RenderRegistry
{

	public static void registerRender()
	{
		addBlockRender(BlockRegistry.soulCage, 0, Reference.MODID + ":soulcage_unbound", "inventory");
		addBlockRender(BlockRegistry.soulCage, 1, Reference.MODID + ":soulcage_inactive", "inventory");
		addBlockRender(BlockRegistry.soulCage, 2, Reference.MODID + ":soulcage_active", "inventory");

		addItemRender(ItemRegistry.soulShard, 0, Reference.MODID + ":unbound_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 1, Reference.MODID + ":tier0_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 2, Reference.MODID + ":tier1_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 3, Reference.MODID + ":tier2_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 4, Reference.MODID + ":tier3_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 5, Reference.MODID + ":tier4_soulshard", "inventory");
		addItemRender(ItemRegistry.soulShard, 6, Reference.MODID + ":tier5_soulshard", "inventory");
	}

	private static void addBlockRender(net.minecraft.block.Block block, int metadata, String blockString,
			String location)
	{
		ModelLoader.setCustomModelResourceLocation(net.minecraft.item.Item.getItemFromBlock(block), metadata,
				new ModelResourceLocation(blockString, location));
	}

	private static void addItemRender(net.minecraft.item.Item item, int metadata, String blockString, String location)
	{
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(blockString, location));
	}
}

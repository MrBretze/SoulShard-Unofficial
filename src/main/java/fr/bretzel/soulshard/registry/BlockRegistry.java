package fr.bretzel.soulshard.registry;

import fr.bretzel.soulshard.block.SoulCage;
import fr.bretzel.soulshard.item.SoulCageItem;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry
{

	public static SoulCage soulCage;

	public static void registerBlock()
	{
		GameRegistry.registerBlock(soulCage = new SoulCage("soul_cage", Material.rock, 3, 15), SoulCageItem.class,
				"soul_cage");
		GameRegistry.registerTileEntity(SoulCageTileEntity.class, "tileSoulCageEntity");
	}
}

package fr.bretzel.soulshard.register;

import fr.bretzel.soulshard.block.SoulCage;
import fr.bretzel.soulshard.itemblock.SoulCageItemBlockMeta;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Block {

    public static SoulCage soulCage;

    public static void registerBlock() {
        GameRegistry.registerBlock(soulCage = new SoulCage("soul_cage", Material.rock, 3, 15), SoulCageItemBlockMeta.class, "soul_cage");
        GameRegistry.registerTileEntity(SoulCageTileEntity.class, "tileSoulCageEntity");
    }
}

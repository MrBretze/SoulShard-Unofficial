package fr.bretzel.soulshard.register;

import fr.bretzel.soulshard.Reference;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class Render {

    public static void registerRender() {
        addBlockRender(Block.soulCage, 0, Reference.MODID + ":soulcage_unbound", "inventory");
        addBlockRender(Block.soulCage, 1, Reference.MODID + ":inactive_soulcage", "inventory");
        addBlockRender(Block.soulCage, 2, Reference.MODID + ":active_soulcage", "inventory");
    }

    public static void addBlockRender(net.minecraft.block.Block block, int metadata, String blockString, String location) {
        ModelLoader.setCustomModelResourceLocation(net.minecraft.item.Item.getItemFromBlock(block), metadata, new ModelResourceLocation(blockString, location));
    }
}

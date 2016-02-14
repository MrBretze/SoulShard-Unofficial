package fr.bretzel.soulshard.register;

import fr.bretzel.soulshard.Reference;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class Render {

    public static void registerRender() {
        addBlockRender(Block.soulCage, 0, Reference.MODID + ":soulcage_unbound", "inventory");
        addBlockRender(Block.soulCage, 1, Reference.MODID + ":inactive_soulcage", "inventory");
        addBlockRender(Block.soulCage, 2, Reference.MODID + ":active_soulcage", "inventory");

        addItemRender(Item.soulShard, 0, Reference.MODID + ":unbound_soulshard", "inventory");
        addItemRender(Item.soulShard, 1, Reference.MODID + ":tier0_soulshard", "inventory");
        addItemRender(Item.soulShard, 2, Reference.MODID + ":tier1_soulshard", "inventory");
        addItemRender(Item.soulShard, 3, Reference.MODID + ":tier2_soulshard", "inventory");
        addItemRender(Item.soulShard, 4, Reference.MODID + ":tier3_soulshard", "inventory");
        addItemRender(Item.soulShard, 5, Reference.MODID + ":tier4_soulshard", "inventory");
        addItemRender(Item.soulShard, 6, Reference.MODID + ":tier5_soulshard", "inventory");
    }

    private static void addBlockRender(net.minecraft.block.Block block, int metadata, String blockString, String location) {
        ModelLoader.setCustomModelResourceLocation(net.minecraft.item.Item.getItemFromBlock(block), metadata, new ModelResourceLocation(blockString, location));
    }

    private static void addItemRender(net.minecraft.item.Item item, int metadata, String blockString, String location) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(blockString, location));
    }
}

package fr.bretzel.soulshard.registry;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingRegistry {


    public static void registerCraft() {
        //TODO: New system !
        GameRegistry.addRecipe(new ItemStack(BlockRegistry.soulCage, 1, 0), "OIO", "ISI", "OIO", 'O', Blocks.obsidian, 'I', Blocks.iron_bars, 'S', ItemRegistry.soulShard);
        GameRegistry.addSmelting(Items.diamond, new ItemStack(ItemRegistry.soulShard, 1, 0), 100);
    }
}

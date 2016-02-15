package fr.bretzel.soulshard;

import fr.bretzel.soulshard.registry.BlockRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


public class SoulCreativeTab extends CreativeTabs {

    public SoulCreativeTab() {
        super("soul_shard");
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(BlockRegistry.soulCage);
    }

    @Override
    public int getIconItemDamage() {
        return 2;
    }
}

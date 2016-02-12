package fr.bretzel.soulshard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


public class SoulCreativeTab extends CreativeTabs {

    public SoulCreativeTab() {
        super(Reference.MODID);
    }

    @Override
    public Item getTabIconItem() {
        return Items.item_frame;
    }
}

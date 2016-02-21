package fr.bretzel.soulshard.item;

import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.meta.IMetaBlockName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class SoulCageItem extends ItemBlock {

    public SoulCageItem(Block block) {
        super(block);
        if (!(block instanceof IMetaBlockName)) {
            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of ISpecialBlockName!", block.getUnlocalizedName()));
        }
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + ((IMetaBlockName) this.block).getSpecialName(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (Utils.hasTagCompound(stack) && !Utils.getDisplayName(stack).equals("null")) {
            return super.getItemStackDisplayName(stack) + " (" + EnumChatFormatting.YELLOW + Utils.getDisplayName(stack) + EnumChatFormatting.RESET + ")";
        }

        return super.getItemStackDisplayName(stack);
    }
}

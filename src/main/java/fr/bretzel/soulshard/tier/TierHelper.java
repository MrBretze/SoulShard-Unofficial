package fr.bretzel.soulshard.tier;

import net.minecraft.item.ItemStack;

public class TierHelper {

    public static final int MAX_MOB_KILL = 1024;

    public static void checkShard(ItemStack stack) {

    }

    public int tierToDamge(ItemStack stack) {
        if (stack.hasTagCompound())
            return stack.getItemDamage();

        if (stack.getItemDamage() == 1 && stack.getTagCompound().getInteger("Tier") == 1) {
            return 2;
        }

        if (stack.getItemDamage() == 1 && stack.getTagCompound().getInteger("Tier") == 2) {
            return 3;
        }

        if (stack.getItemDamage() == 1 && stack.getTagCompound().getInteger("Tier") == 3) {
            return 4;
        }

        if (stack.getItemDamage() == 1 && stack.getTagCompound().getInteger("Tier") == 4) {
            return 5;
        }
        if (stack.getItemDamage() == 1 && stack.getTagCompound().getInteger("Tier") == 5) {
            return 6;
        }

        return stack.getItemDamage();
    }
}

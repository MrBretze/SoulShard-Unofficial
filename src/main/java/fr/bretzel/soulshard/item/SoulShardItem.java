package fr.bretzel.soulshard.item;

import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SoulShardItem extends Item {


    public SoulShardItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);

        this.setMaxStackSize(1);
        this.setCreativeTab(CommonRegistry.creativeTab);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (Utils.hasTagCompound(stack) && !Utils.getDisplayName(stack).equals("null")) {
            return super.getItemStackDisplayName(stack) + " (" + EnumChatFormatting.YELLOW + Utils.getDisplayName(stack) + EnumChatFormatting.RESET + ")";
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = stack.getItemDamage();
        for (EnumType type : EnumType.META_LOOKUP) {
            if (type.getDamage() == damage) {
                return super.getUnlocalizedName(stack) + "." + type.getName();
            }
        }
        return super.getUnlocalizedName(stack);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (EnumType type : EnumType.META_LOOKUP) {
            ItemStack stack = new ItemStack(itemIn, 1, type.getDamage());
            if (!stack.hasTagCompound()) {
                Utils.initShard(stack);
                subItems.add(stack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() == 6;
    }

    public static void increaseShardKillCount(ItemStack stack, int kill) {

        if (!Utils.hasTagCompound(stack))
            return;

        if (!stack.getTagCompound().hasKey(Utils.KILL_COUNT))
            return;

        Utils.addKillCount(stack, kill);

        Utils.checkAndFixShard(stack);
    }

    public static ItemStack getShardFromInventory(EntityPlayer player, String entity) {
        ItemStack lastResort = null;
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof SoulShardItem && Utils.getTierForStack(stack) <= 5) {
                if (!Utils.isBound(stack) && lastResort == null) {
                    lastResort = stack;
                } else if (Utils.getEntityType(stack).equals(entity)){
                    return stack;
                }
            }
        }

        return lastResort;
    }

    public enum EnumType implements IStringSerializable {

        UNBOUND(-1, 0, "unbound"),
        TIER_0(0, 1, "tier0"),
        TIER_1(1, 2, "tier1"),
        TIER_2(2, 3, "tier2"),
        TIER_3(3, 4, "tier3"),
        TIER_4(4, 5, "tier4"),
        TIER_5(5, 6, "tier5");

        private int damage;
        private int tier;
        private String name;
        private static EnumType[] META_LOOKUP;

        static {
            int i = 0;
            META_LOOKUP = new EnumType[values().length];
            for (EnumType type : values()) {
                META_LOOKUP[i] = type;
                i++;
            }
        }

        private EnumType(int tier, int damage, String name) {
            this.damage = damage;
            this.name = name;
            this.tier = tier;
        }

        public int getTier() {
            return tier;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        public int getDamage() {
            return damage;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static EnumType[] getMetaLookup() {
            return META_LOOKUP;
        }
    }

}

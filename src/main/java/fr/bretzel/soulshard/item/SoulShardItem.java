package fr.bretzel.soulshard.item;

import fr.bretzel.soulshard.register.Common;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SoulShardItem extends Item {

    public String resource_name = "";

    public SoulShardItem(String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.resource_name = unlocalizedName;

        this.setMaxStackSize(1);
        this.setCreativeTab(Common.creativeTab);
        this.setHasSubtypes(true);
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = stack.getItemDamage();
        for (EnumType type : EnumType.META_LOOKUP) {
            if (type.getDamage() == damage) {
                if (stack.hasTagCompound()) {
                    NBTTagCompound compound = stack.getTagCompound();
                    if (compound.hasKey("EntityType")) {
                        if (compound.getString("EntityType").isEmpty() || compound.getString("EntityType").equals("empty")) {
                            return super.getUnlocalizedName(stack) + "." + type.getName();
                        } else {
                            return super.getUnlocalizedName(stack) + "." + type.getName() +
                                    "(" + EnumChatFormatting.DARK_PURPLE  + compound.getString("EntityType") + EnumChatFormatting.RESET + ")";
                        }
                    }
                } else {
                    return super.getUnlocalizedName(stack) + "." + type.getName();
                }
            }
        }
        return super.getUnlocalizedName(stack);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (EnumType type : EnumType.META_LOOKUP) {
            ItemStack stack = new ItemStack(itemIn, 1, type.getDamage());
            if (type != EnumType.UNBOUND && !stack.hasTagCompound()) {
                //TODO: USE TierHelper.class
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("EntityType", "empty");
                compound.setInteger("Tier", type.getTier());
                compound.setInteger("KillCount", 0);
                stack.setTagCompound(compound);
                subItems.add(stack);
            }
            if (type == EnumType.UNBOUND) {
                subItems.add(new ItemStack(itemIn, 1, EnumType.UNBOUND.getDamage()));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() == 6;
    }

    public static enum EnumType implements IStringSerializable {

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
    }

}

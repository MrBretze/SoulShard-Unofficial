package fr.bretzel.soulshard.item;

import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.tier.TierHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && compound.hasKey("EntityType") && !compound.getString("EntityType").equals("empty"))
            return super.getItemStackDisplayName(stack) + " (" + EnumChatFormatting.DARK_PURPLE + compound.getString("EntityType") + EnumChatFormatting.RESET + ")";
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
                //TODO: USE TierHelper.class
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("EntityType", "empty");
                compound.setInteger("Tier", type.getTier());
                compound.setInteger("KillCount", 0);
                stack.setTagCompound(compound);
                subItems.add(stack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() == 6;
    }

    public static void boundEntity(EntityLiving entityLiving, ItemStack stack) {
        if (!isSoulShard(stack))
            return;

        if (isBound(stack))
            return;

        if (SoulShard.mobMapping.isMobBlackListed(entityLiving))
            return;

        NBTTagCompound compound = new NBTTagCompound();

        if (stack.getItemDamage() == EnumType.UNBOUND.getDamage()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("KillCount", 0);
            stack.getTagCompound().setString("EntityType", "empty");
            stack.getTagCompound().setInteger("Tier", 0);
            stack.setItemDamage(1);
        }

        if (stack.getItemDamage() != EnumType.UNBOUND.getDamage()) {

            entityLiving.writeEntityToNBT(compound);

            stack.getTagCompound().setTag("EntitySaved", compound);
            stack.getTagCompound().setString("EntityType", SoulShard.mobMapping.getEntityType(entityLiving));
        }
    }

    public static void increaseShardKillCount(ItemStack stack, int kill) {

        if (!stack.hasTagCompound())
            return;

        if (!stack.getTagCompound().hasKey("KillCount"))
            return;

        stack.getTagCompound().setInteger("KillCount", stack.getTagCompound().getInteger("KillCount") + kill);

        TierHelper.checkShard(stack);
    }

    public static String getEntityTypeFromStack(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityType") && !stack.getTagCompound().getString("EntityType").equals("empty")) {
            return stack.getTagCompound().getString("EntityType");
        }

        return "empty";
    }

    public static boolean isBound(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityType") && !stack.getTagCompound().getString("EntityType").equals("empty");
    }

    public static ItemStack getShardFromInventory(EntityPlayer player, String entity) {
        ItemStack lastResort = null;
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof SoulShardItem && stack.getTagCompound().getInteger("KillCount") <= TierHelper.MAX_MOB_KILL) {
                if (!isBound(stack) && lastResort == null) {
                    lastResort = stack;
                } else if (getEntityTypeFromStack(stack).equals(entity)){
                    return stack;
                }
            }
        }

        return lastResort;
    }

    public static boolean isSoulShard(ItemStack stack) {
        return stack.getItem() instanceof SoulShardItem;
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

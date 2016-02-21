package fr.bretzel.soulshard;

import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Utils {

    public static final String KILL_COUNT = "KillCount";
    public static final String ENTITY_TYPE = "EntityType";
    public static final String ENTITY_SAVED = "EntitySaved";
    public static final String TIER = "Tier";
    public static final String DISPLAY_NAME = "DisplayName";

    private static final int[] KILL_MOB = {64, 128, 256, 512, 1024};

    public static void checkAndFixShard(ItemStack stack) {

        if (!(stack.getItem() instanceof SoulShardItem)) {
            throw new IllegalArgumentException("Only use a SoulShard item for this method !");
        }

        if (!hasTagCompound(stack)) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(KILL_COUNT, 0);
            stack.getTagCompound().setString(ENTITY_TYPE, "null");
            stack.getTagCompound().setInteger(TIER, 0);
            stack.getTagCompound().setString(DISPLAY_NAME, "null");
            stack.getTagCompound().setTag(ENTITY_SAVED, new NBTTagCompound());
        }

        int tier = getTier(stack);

        if (getKillCount(stack) >= getMaxKillForTier(tier) && tier <= 5)
            setTier(stack, tier + 1);

        if (stack.getItemDamage() != getDamageForTier(getTier(stack)))
            stack.setItemDamage(getDamageForTier(getTier(stack)));
    }

    public static void initShard(ItemStack stack) {
        if (!hasTagCompound(stack)) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(KILL_COUNT, 0);
            stack.getTagCompound().setString(ENTITY_TYPE, "null");
            stack.getTagCompound().setInteger(TIER, 0);
            stack.getTagCompound().setString(DISPLAY_NAME, "null");
            stack.getTagCompound().setTag(ENTITY_SAVED, new NBTTagCompound());
        }
    }

    public static void boundEntity(EntityLiving entityLiving, ItemStack stack) {
        if (!isSoulShard(stack))
            return;

        if (isBound(stack))
            return;

        if (SoulShard.mobMapping.isMobBlackListed(entityLiving))
            return;

        NBTTagCompound compound = new NBTTagCompound();

        if (stack.getItemDamage() == SoulShardItem.EnumType.UNBOUND.getDamage()) {
            stack.setItemDamage(1);
            Utils.checkAndFixShard(stack);
        }

        if (stack.getItemDamage() != SoulShardItem.EnumType.UNBOUND.getDamage()) {

            entityLiving.writeEntityToNBT(compound);

            stack.getTagCompound().setTag(ENTITY_SAVED, compound);
            stack.getTagCompound().setString(ENTITY_TYPE, SoulShard.mobMapping.getEntityType(entityLiving));
            stack.getTagCompound().setString(DISPLAY_NAME, entityLiving.getCommandSenderEntity().getName());
        }
    }

    public static boolean isSoulShard(ItemStack stack) {
        return stack.getItem() instanceof SoulShardItem;
    }

    public static boolean isBound(ItemStack stack) {
        return hasTagCompound(stack) && stack.getTagCompound().hasKey(ENTITY_TYPE) && !stack.getTagCompound().getString(ENTITY_TYPE).equals("null");
    }

    public static int getMaxKillForTier(int tier) {
        return KILL_MOB[tier];
    }

    public static String getEntityType(ItemStack stack) {
        if (!hasTagCompound(stack))
            return "null";

        return stack.getTagCompound().getString(ENTITY_TYPE);
    }

    public static int getTierForStack(ItemStack stack) {
        if (!hasTagCompound(stack))
            return 0;

        return getTier(stack);
    }

    public static int getDamageForTier(int tier) {
        for (SoulShardItem.EnumType type : SoulShardItem.EnumType.getMetaLookup()) {
            if (type.getTier() == tier)
                return type.getDamage();
        }
        return SoulShardItem.EnumType.TIER_0.getDamage();
    }

    public static String getDisplayName(ItemStack stack) {
        if (!hasTagCompound(stack))
            return "null";

        return stack.getTagCompound().getString(DISPLAY_NAME);
    }

    public static int getKillCount(ItemStack stack) {
        if (!hasTagCompound(stack))
            return 0;

        return stack.getTagCompound().getInteger(KILL_COUNT);
    }

    public static void setKillCount(ItemStack stack, int i) {
        stack.getTagCompound().setInteger(KILL_COUNT, i);
    }

    public static void addKillCount(ItemStack stack, int killCount) {
        setKillCount(stack, getKillCount(stack) + killCount);
    }

    public static boolean hasTagCompound(ItemStack stack) {
        return stack.hasTagCompound();
    }

    public static void setTier(ItemStack stack, int tier) {
        if (!hasTagCompound(stack))
            return;

        stack.getTagCompound().setInteger(TIER, tier);
    }

    public static int getTier(ItemStack stack) {
        if (!hasTagCompound(stack)) {
            return 0;
        }

        return stack.getTagCompound().getInteger(TIER);
    }

    public static boolean isBound(SoulCageTileEntity tileEntity) {
        return tileEntity.getTileData().hasKey(ENTITY_TYPE) && !tileEntity.getTileData().getString(ENTITY_TYPE).equals("null");
    }

    public static void boundEntity(SoulCageTileEntity tileEntity) {

        //TODO: Init this !

        //tileEntity.
    }
}

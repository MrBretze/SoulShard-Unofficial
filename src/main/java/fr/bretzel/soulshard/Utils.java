package fr.bretzel.soulshard;

import fr.bretzel.soulshard.item.SoulShardItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static final String KILL_COUNT = "KillCount";
    public static final String ENTITY_TYPE = "EntityType";
    public static final String TIER = "Tier";
    public static final String DISPLAY_NAME = "DisplayName";

    private static final int[] KILL_MOB = {64, 128, 256, 512, 1024};
    private static final int[] TIME = {13, 10, 7, 5, 3};
    private static final int[] SPAWN_MOB = {2, 4, 4, 4, 6};
    private static final boolean[] NEED_REDSTONE = {false, false, true, true, true};

    public static void checkAndFixShard(ItemStack stack) {

        if (!isSoulShard(stack)) {
            throw new IllegalArgumentException("Only use a SoulShard item for this method !");
        }

        if (!hasTagCompound(stack)) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger(KILL_COUNT, 0);
            stack.getTagCompound().setString(ENTITY_TYPE, "null");
            stack.getTagCompound().setInteger(TIER, 0);
            stack.getTagCompound().setString(DISPLAY_NAME, "null");
        }

        int tier = getTier(stack);

        if (getKillCount(stack) >= getMaxKillForTier(tier) && tier <= 5) {

            tier++;

            if (tier > 5)
                tier = 5;

            setTier(stack, tier);
        }

        if (getKillCount(stack) > getMaxKillForTier(5))
            setKillCount(stack, getMaxKillForTier(5));

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
        }
    }

    public static void boundEntity(EntityLiving entityLiving, ItemStack stack) {
        if (!isSoulShard(stack) || isBound(stack) || SoulShard.mobMapping.isMobBlackListed(entityLiving))
            return;

        NBTTagCompound compound = new NBTTagCompound();

        if (stack.getItemDamage() == SoulShardItem.EnumType.UNBOUND.getDamage()) {
            stack.setItemDamage(1);
            Utils.checkAndFixShard(stack);
        }

        if (stack.getItemDamage() != SoulShardItem.EnumType.UNBOUND.getDamage()) {

            entityLiving.writeEntityToNBT(compound);

            stack.getTagCompound().setString(ENTITY_TYPE, SoulShard.mobMapping.getEntityType(entityLiving));
            stack.getTagCompound().setString(DISPLAY_NAME, entityLiving.getCommandSenderEntity().getName());
        }
    }

    public static int getEntitySpawnForTier(int tier) {
        if (tier == 5)
            tier = 4;

        return SPAWN_MOB[tier];
    }

    public static boolean isSoulShard(ItemStack stack) {
        return stack.getItem() instanceof SoulShardItem;
    }

    public static boolean isBound(ItemStack stack) {
        return hasTagCompound(stack) && stack.getTagCompound().hasKey(ENTITY_TYPE) && !stack.getTagCompound().getString(ENTITY_TYPE).equals("null");
    }

    public static int getMaxKillForTier(int tier) {
        if (tier == 5)
            tier = 4;

        return KILL_MOB[tier];
    }

    public static int getTime(int tier) {
        if (tier == 5)
            tier = 4;

        return TIME[tier];
    }

    public static boolean needRedstone(int tier) {
        if (tier == 5)
            tier = 4;

        return NEED_REDSTONE[tier];
    }

    public static String getEntityType(ItemStack stack) {
        if (!hasTagCompound(stack))
            return "null";

        return stack.getTagCompound().getString(ENTITY_TYPE);
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
        if (!hasTagCompound(stack))
            return;

        stack.getTagCompound().setInteger(KILL_COUNT, i);
    }

    public static void addKillCount(ItemStack stack, int killCount) {
        setKillCount(stack, getKillCount(stack) + killCount);
    }

    public static boolean hasTagCompound(ItemStack stack) {
        return stack != null && stack.hasTagCompound();
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

    public static void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            SoulShard.soulLog.warn(e.fillInStackTrace());
        }
    }
}

package fr.bretzel.soulshard;

import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static final String KILL_COUNT = "KillCount";
    public static final String ENTITY_TYPE = "EntityType";
    public static final String ENTITY_SAVED = "EntitySaved";
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
            stack.getTagCompound().setTag(ENTITY_SAVED, new NBTTagCompound());
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
            stack.getTagCompound().setTag(ENTITY_SAVED, new NBTTagCompound());
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

            stack.getTagCompound().setTag(ENTITY_SAVED, compound);
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

    public static boolean isBound(SoulCageTileEntity tileEntity) {
        return tileEntity.soul_shard != null && isBound(tileEntity.soul_shard);
    }

    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean bool) {
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;
        double d0 = player.posX;
        double d1 = player.posY + (double) player.getEyeHeight();
        double d2 = player.posZ;
        Vec3 vec3 = new Vec3(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;

        if (player instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return world.rayTraceBlocks(vec3, vec31, bool, !bool, false);
    }

    public static void createNewFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            SoulShard.soulLog.warn(e.fillInStackTrace());
        }
    }
}

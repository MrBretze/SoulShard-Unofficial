package fr.bretzel.soulshard.tileentity;

import fr.bretzel.soulshard.TierUtils;
import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.SoulCage;
import fr.bretzel.soulshard.registry.ItemRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.UUID;

public class SoulCageTileEntity extends TileEntity implements ITickable {

    private ItemStack soul_shard;
    private int tick = 0;
    private int spawnDelay = Integer.MAX_VALUE;
    private UUID owner;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (soul_shard != null) {
            NBTTagCompound ntcp = new NBTTagCompound();
            soul_shard.writeToNBT(ntcp);
            compound.setTag("SoulShard", ntcp);
        }

        compound.setTag("Owner", new NBTTagString(owner.toString()));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("SoulShard"))
            soul_shard = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("SoulShard"));

        owner = UUID.fromString(compound.getString("Owner"));
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 1, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager data, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    public void updateDelay() {
        spawnDelay--;

        int tier = Utils.getTier(soul_shard);

        boolean needCorrectLight = TierUtils.getCheckLight(tier);

        if (spawnDelay <= 0) {

            spawnDelay = TierUtils.getSpawnDelay(tier);
            int b = TierUtils.getNumSpawns(tier);

            for (int i = b; i > 0; i--) {

                Entity entity = EntityList.createEntityByName(Utils.getEntityType(soul_shard), worldObj);

                if (entity == null) {
                    return;
                }

                EntityLiving entityLiving = (EntityLiving) entity;

                if (getNearbyEntity(entityLiving) > 80) {
                    tick = 20;
                    return;
                }

                BlockPos pos = getRandomBlockPos(4);

                entityLiving.setLocationAndAngles(pos.getX(), getPos().getY(), pos.getZ(), worldObj.rand.nextFloat() * 360F, 0.0F);

                if (needCorrectLight && canSpawnInLight(entityLiving)) {
                    continue;
                }

                entityLiving.getEntityData().setBoolean("IsSoulShard", true);

                entityLiving.rotationYawHead = entityLiving.rotationYaw;
                entityLiving.renderYawOffset = entityLiving.rotationYaw;

                entityLiving.onInitialSpawn(worldObj.getDifficultyForLocation(getPos()), null);
                spawnEntity(entityLiving);
                entityLiving.spawnExplosionParticle();
            }
        }

    }

    public void updateSecond() {

        if (!Utils.hasTagCompound(soul_shard))
            return;

        int tier = Utils.getTier(soul_shard);

        if (spawnDelay == Integer.MAX_VALUE) {
            spawnDelay = TierUtils.getSpawnDelay(TierUtils.getValidTier(tier));
        }

        boolean needRedstone = TierUtils.getNeedRedstone(tier);
        boolean isActive = worldObj.isBlockPowered(getPos());
        boolean needPlayer = TierUtils.getPlayerCheck(tier);


        if (needRedstone && isActive) {
            if (needPlayer) {
                if (worldObj.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 16).getUniqueID().toString().equals(owner.toString()))
                    updateDelay();
                else
                    return;
            } else {
                updateDelay();
            }
            return;
        }

        if (!needRedstone) {
            updateDelay();
            return;
        }

        spawnDelay = Integer.MAX_VALUE;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote && soul_shard != null) {

            boolean isActive = worldObj.isBlockPowered(getPos());

            if (!Utils.hasTagCompound(soul_shard))
                return;

            int tier = Utils.getTier(soul_shard);
            boolean needRedstone = TierUtils.getNeedRedstone(tier);

            if (getNearbyEntity((EntityLiving) EntityList.createEntityByName(Utils.getEntityType(soul_shard), worldObj)) >= 80) {
                worldObj.setBlockState(getPos(), getBlockType().getStateFromMeta(SoulCage.EnumType.INACTIVE_SOULCAGE.getDamage()));
                tick = 20;
                return;
            }

            tick--;

            if (tick <= 0) {
                updateSecond();
                worldObj.markBlockForUpdate(getPos());
                markDirty();
                tick = 20;
            }

            if (needRedstone && isActive) {
                if (getBlockMetadata() == 1) {
                    worldObj.setBlockState(getPos(), getBlockType().getStateFromMeta(SoulCage.EnumType.ACTIVE_SOULCAGE.getDamage()));
                    return;
                }
            }

            if (!needRedstone) {
                worldObj.setBlockState(getPos(), getBlockType().getStateFromMeta(SoulCage.EnumType.ACTIVE_SOULCAGE.getDamage()));
                return;
            }

            if (getBlockMetadata() == 2 && !isActive) {
                worldObj.setBlockState(getPos(), getBlockType().getStateFromMeta(SoulCage.EnumType.INACTIVE_SOULCAGE.getDamage()));
            }
        }
    }

    public int getNearbyEntity(EntityLiving entityLiving) {
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(getPos().getX() - 16, getPos().getY() - 16, getPos().getZ() - 16, getPos().getX() + 16, getPos().getY() + 16, getPos().getZ() + 16);

        int mob = 0;

        for (EntityLiving entity : worldObj.getEntitiesWithinAABB(entityLiving.getClass(), aabb)) {
            if (entity.getEntityData().getBoolean("IsSoulShard"))
                mob++;
        }

        return mob;
    }

    public Entity spawnEntity(Entity entity) {
        if (entity.worldObj != null)
            entity.worldObj.spawnEntityInWorld(entity);

        return entity;
    }

    public BlockPos getRandomBlockPos(int range) {

        int x = worldObj.rand.nextInt(range);
        int y = 0;
        int z = worldObj.rand.nextInt(range);

        x = worldObj.rand.nextBoolean() ? -x : x;
        z = worldObj.rand.nextBoolean() ? -z : z;

        BlockPos r = new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ()).add(x, y, z);

        if (getPos().distanceSqToCenter(r.getX(), r.getY(), r.getZ()) <= 1)
            return getRandomBlockPos(range);

        return r;
    }

    public int getTick() {
        return tick;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }

    public ItemStack getSoulShard() {
        return soul_shard;
    }

    public void setSoulShard(ItemStack stack) {
        this.soul_shard = stack;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    /**
     * Tanks modmuss50: (https://github.com/TechReborn/RebornCore/blob/1.8.9/src%2Fmain%2Fjava%2Freborncore%2Fcommon%2Ftile%2FTileMachineBase.java#L74)
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos blockPos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public ItemStack getSoulShardStack() {
        ItemStack stack = new ItemStack(ItemRegistry.soulShard, 1, soul_shard.getItemDamage());
        stack.setTagCompound(soul_shard.getTagCompound());
        return stack;
    }

    public boolean canSpawnInLight(EntityLiving living) {
        int light = worldObj.getLight(getPos());

        if (living instanceof EntityMob || living instanceof IMob)
            return light <= 8;

        if (living instanceof EntityAnimal || living instanceof IAnimals)
            return light > 8;

        return true;
    }
}

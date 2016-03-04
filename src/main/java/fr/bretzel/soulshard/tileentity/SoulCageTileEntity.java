package fr.bretzel.soulshard.tileentity;

import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.SpawnerManager;
import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.SoulCage;
import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public class SoulCageTileEntity extends TileEntity implements ITickable {

    public ItemStack soul_shard;
    public boolean isActive = false;
    public int tick = 20;
    public int spawnDelay = Integer.MAX_VALUE;
    public UUID uuid;

    public SpawnerManager spawnerManager;

    public SoulCageTileEntity() {
        this.uuid = UUID.randomUUID();
        spawnerManager = new SpawnerManager(this);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("Active", isActive);
        compound.setInteger("Ticks", tick);
        compound.setString("UUID", uuid.toString());

        if (soul_shard != null) {
            NBTTagCompound ntcp = new NBTTagCompound();
            soul_shard.writeToNBT(ntcp);
            compound.setTag("SoulShard", ntcp);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        isActive = compound.getBoolean("Active");
        tick = compound.getInteger("Ticks");
        uuid = UUID.fromString(compound.getString("UUID"));

        if (compound.hasKey("SoulShard"))
            soul_shard = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("SoulShard"));

    }

    public void updateDelay() {
        spawnDelay--;

        int tier = Utils.getTier(soul_shard);

        if (spawnDelay <= 0) {

            spawnDelay = Utils.getTime(tier);
            int b = Utils.getEntitySpawnForTier(tier);

            for (int i = b; i > 0; i--) {

                Entity entity = EntityList.createEntityByName(Utils.getEntityType(soul_shard), worldObj);

                if (entity == null) {
                    return;
                }

                if (entity instanceof EntityLiving) {
                    EntityLiving entityLiving = (EntityLiving) entity;
                    BlockPos p = getRandomBlockPos(4);
                    SoulShard.soulLog.info(getPos().distanceSqToCenter(p.getX(), p.getY(), p.getZ()));
                    entityLiving.setLocationAndAngles(p.getX(), p.getY(), p.getZ(), worldObj.rand.nextFloat() * 360F, 0.0F);
                    entityLiving.getEntityData().setBoolean("IsSoulShard", true);
                    spawnEntity(entityLiving);
                    entityLiving.spawnExplosionParticle();
                    entityLiving.setHealth(0.0F);
                }
            }
        }
    }

    public void updateSecond() {

        if (!Utils.hasTagCompound(soul_shard))
            return;

        int tier = Utils.getTier(soul_shard);

        if (spawnDelay == Integer.MAX_VALUE) {
            spawnDelay = Utils.getTime(tier);
        }

        boolean needRedstone = Utils.needRedstone(tier);

        if (needRedstone && isActive) {
            updateDelay();
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
        if (!worldObj.isRemote) {

            if (!Utils.hasTagCompound(soul_shard))
                return;

            int tier = Utils.getTier(soul_shard);
            boolean needRedstone = Utils.needRedstone(tier);

            tick--;

            if (tick <= 0) {
                updateSecond();
                worldObj.markBlockForUpdate(getPos());
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
                return;
            }
        }
    }

    public BlockPos getRandomBlockPos(int range) {
        Random random = new Random();

        int x = random.nextInt(range);
        int y = 0;
        int z = random.nextInt(range);

        x = random.nextBoolean() ? -x : x;
        z = random.nextBoolean() ? -z : z;

        BlockPos r = new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ()).add(x, y, z);

        if (getPos().distanceSqToCenter(r.getX(), r.getY(), r.getZ()) <= 1)
            return getRandomBlockPos(range);

        return r;
    }

    public Entity spawnEntity(Entity entity) {
        if (entity.worldObj != null)
            entity.worldObj.spawnEntityInWorld(entity);

        return entity;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 1, tagCompound);
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
}

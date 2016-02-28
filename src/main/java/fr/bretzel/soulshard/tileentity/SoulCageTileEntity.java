package fr.bretzel.soulshard.tileentity;

import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.SoulCage;
import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.UUID;

public class SoulCageTileEntity extends TileEntity implements ITickable {

    public ItemStack soul_shard;
    public UUID owner;
    public boolean isActive = false;
    public int tick = 20;
    public int spawnDelay = Integer.MAX_VALUE;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("Active", isActive);
        compound.setInteger("Ticks", tick);

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

        if (compound.hasKey("SoulShard"))
            soul_shard = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("SoulShard"));

    }

    public void updateDelay(boolean needRedstone) {
        spawnDelay--;
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
            updateDelay(true);
            return;
        }

        if (!needRedstone) {
            if (getBlockMetadata() == 1) {
                worldObj.setBlockState(getPos(), getBlockType().getStateFromMeta(SoulCage.EnumType.UNBOUND_SOULCAGE.getDamage()));
            }
            updateDelay(false);
            return;
        }

        spawnDelay = Integer.MAX_VALUE;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            tick--;
            if (tick <= 0) {
                updateSecond();
                tick = 20;
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 1, tagCompound);
    }

    public ItemStack getSoulShardStack() {
        ItemStack stack = new ItemStack(ItemRegistry.soulShard, 1, soul_shard.getItemDamage());
        stack.setTagCompound(soul_shard.getTagCompound());
        return stack;
    }
}

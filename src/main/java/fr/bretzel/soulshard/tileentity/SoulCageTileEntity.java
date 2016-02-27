package fr.bretzel.soulshard.tileentity;

import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.UUID;

public class SoulCageTileEntity extends TileEntity {

    public ItemStack soul_shard;
    public UUID owner;
    public boolean isActive = false;

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setBoolean("Active", isActive);

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

        if (compound.hasKey("SoulShard"))
            soul_shard = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("SoulShard"));

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

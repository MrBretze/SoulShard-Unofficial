package fr.bretzel.soulshard.tileentity;

import fr.bretzel.soulshard.registry.ItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

import java.util.UUID;

public class SoulCageTileEntity extends TileEntity implements IInventory {

    public ItemStack soul_shard;
    public UUID owner;
    public boolean isActive = false;
    public boolean hasShard = false;

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

        if (compound.hasKey("SoulShard")) {
            soul_shard = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("SoulShard"));

            if (soul_shard != null)
                hasShard = true;

        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 1, tagCompound);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return soul_shard;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (j == 0) return null;

        ItemStack stack = new ItemStack(ItemRegistry.soulShard, 1);
        stack.setTagCompound(soul_shard.getTagCompound());
        setInventorySlotContents(i, null);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        soul_shard = itemStack;

        if (itemStack == null) {
            hasShard = false;
        } else {
            hasShard = true;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        if (stack.getItem() != ItemRegistry.soulShard || !stack.hasTagCompound()) return false;
        NBTTagCompound nbt = stack.getTagCompound();
        return (soul_shard == null && nbt.getInteger("Tier") > 0 && !nbt.getString("EntityType").equals("null"));
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}

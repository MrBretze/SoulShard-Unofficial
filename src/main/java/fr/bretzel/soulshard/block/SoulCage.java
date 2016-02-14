package fr.bretzel.soulshard.block;


import fr.bretzel.soulshard.block.meta.IMetaBlockName;
import fr.bretzel.soulshard.register.Common;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class SoulCage extends Block implements IMetaBlockName {

    public static final PropertyEnum METADATA = PropertyEnum.create("type", SoulCage.EnumType.class);

    public String resource_name = "";

    public SoulCage(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);

        resource_name = unlocalizedName;
        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(Common.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, EnumType.UNBOUND_SOULCAGE));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, METADATA);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public IBlockState getStateFromMeta(int damage) {
        return getDefaultState().withProperty(METADATA, EnumType.byMetadata(damage));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType type = (EnumType) state.getValue(METADATA);
        return type.getDamage();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        int damage = stack.getItemDamage();
        for (EnumType type : EnumType.META_LOOKUP) {
            if (type.getDamage() == damage)
                return type.name;
        }
        return EnumType.UNBOUND_SOULCAGE.getName();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return new ItemStack(Item.getItemFromBlock(this), 1,0);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new SoulCageTileEntity(world, this, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing facing, float par7, float par8, float par9) {
        if (!world.isRemote) {

            SoulCageTileEntity tile = (SoulCageTileEntity) world.getTileEntity(blockPos);

            if (tile != null) {

                if (player.getHeldItem() != null && player.getHeldItem().getItem() == fr.bretzel.soulshard.register.Item.soulShard) {

                    ItemStack stack = player.getHeldItem();

                    if (stack.hasTagCompound()) {

                        NBTTagCompound compound = stack.getTagCompound();
                        int tier = compound.getInteger("Tier");
                        String entName = compound.getString("EntityType");

                        if (tier == 0 || entName.isEmpty() || entName.equals("empty"))
                            return false;

                        tile.setInventorySlotContents(0, stack);
                        tile.owner = player.getUniqueID();

                        if(!player.capabilities.isCreativeMode)
                            stack.stackSize--;
                    }
                }

                if (player.isSneaking() && player.getHeldItem() == null) {
                    if (tile.soul_shard != null) {
                        Entity entity = new EntityItem(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), tile.soul_shard);
                        world.spawnEntityInWorld(entity);
                    }
                }
            }
        }
        return false;
    }

    public static enum EnumType implements IStringSerializable {

        UNBOUND_SOULCAGE(0, "unbound_soulcage"),
        INACTIVE_SOULCAGE(1, "inactive_soulcage"),
        ACTIVE_SOULCAGE(2, "active_soulcage");

        private int damage;
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

        private EnumType(int id, String name) {
            this.damage = id;
            this.name = name;
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
            if(meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }
    }
}

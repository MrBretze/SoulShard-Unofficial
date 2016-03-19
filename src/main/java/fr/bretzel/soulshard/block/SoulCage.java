package fr.bretzel.soulshard.block;


import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.meta.IMetaBlockName;
import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class SoulCage extends Block implements IMetaBlockName {

    public static final PropertyEnum METADATA = PropertyEnum.create("type", SoulCage.EnumType.class);

    public SoulCage(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(CommonRegistry.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, EnumType.UNBOUND_SOULCAGE));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof SoulCageTileEntity) {
            SoulCageTileEntity tileEntity = (SoulCageTileEntity) world.getTileEntity(pos);
            if (tileEntity.getSoulShard() != null)
                world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, tileEntity.getSoulShardStack()));
            world.removeTileEntity(pos);
        }
    }

    @Override
    public IBlockState onBlockPlaced(World p_onBlockPlaced_1_, BlockPos p_onBlockPlaced_2_, EnumFacing p_onBlockPlaced_3_, float p_onBlockPlaced_4_, float p_onBlockPlaced_5_, float p_onBlockPlaced_6_, int p_onBlockPlaced_7_, EntityLivingBase p_onBlockPlaced_8_) {
        return super.onBlockPlaced(p_onBlockPlaced_1_, p_onBlockPlaced_2_, p_onBlockPlaced_3_, p_onBlockPlaced_4_, p_onBlockPlaced_5_, p_onBlockPlaced_6_, p_onBlockPlaced_7_, p_onBlockPlaced_8_);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, METADATA);
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
    public boolean isFullyOpaque(IBlockState p_isFullyOpaque_1_) {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumType) state.getValue(METADATA)).getDamage();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult result, World world, BlockPos blockPos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new SoulCageTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand enumHand, ItemStack itemStack, EnumFacing enumFacing, float p_onBlockActivated_8_, float p_onBlockActivated_9_, float p_onBlockActivated_10_) {
        if (!world.isRemote) {

            TileEntity tile = world.getTileEntity(blockPos);

            if (tile == null || !(tile instanceof SoulCageTileEntity))
                return false;

            SoulCageTileEntity soulTile = (SoulCageTileEntity) tile;

            if (player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof SoulShardItem && soulTile.getSoulShard() == null) {

                ItemStack soulStack = player.getHeldItem(EnumHand.MAIN_HAND);

                if (Utils.isBound(soulStack)) {

                    int tier = Utils.getTier(soulStack);
                    String entName = Utils.getEntityType(soulStack);

                    if (tier == 0 || entName.isEmpty() || entName.equals("null"))
                        return false;

                    soulTile.setSoulShard(soulStack);

                    world.setBlockState(blockPos, getStateFromMeta(EnumType.INACTIVE_SOULCAGE.getDamage()));

                    if (!player.capabilities.isCreativeMode)
                        soulStack.stackSize--;

                    return true;
                }
            }

            if (player.getHeldItem(EnumHand.MAIN_HAND) == null && player.isSneaking()) {
                if (soulTile.getSoulShard() != null) {
                    world.spawnEntityInWorld(new EntityItem(world, blockPos.getX() + 0.5, blockPos.getY() + 0.6, blockPos.getZ() + 0.5, soulTile.getSoulShardStack()));
                    world.setBlockState(blockPos, getStateFromMeta(EnumType.UNBOUND_SOULCAGE.getDamage()));
                    soulTile.setSoulShard(null);
                }

                return true;
            }
        }
        return false;
    }

    public enum EnumType implements IStringSerializable {

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
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }
    }
}

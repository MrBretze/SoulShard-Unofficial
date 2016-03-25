package fr.bretzel.soulshard.block;

import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.Utils;
import fr.bretzel.soulshard.block.meta.IMetaBlockName;
import fr.bretzel.soulshard.item.SoulShardItem;
import fr.bretzel.soulshard.registry.CommonRegistry;
import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;

import net.minecraft.block.BlockContainer;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoulCage extends BlockContainer implements IMetaBlockName
{

	public static final PropertyEnum METADATA = PropertyEnum.create("type", SoulCage.EnumType.class);

	public SoulCage(String unlocalizedName, Material material, float hardness, float resistance)
	{
		super(material);

		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setCreativeTab(CommonRegistry.creativeTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, EnumType.UNBOUND_SOULCAGE));
	}

	@Override public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof SoulCageTileEntity)
		{
			SoulCageTileEntity tileEntity = (SoulCageTileEntity) world.getTileEntity(pos);
			if (tileEntity.getSoulShard() != null)
			{
				ItemStack stack = new ItemStack(Item.getItemFromBlock(state.getBlock()));
				stack.setItemDamage(EnumType.INACTIVE_SOULCAGE.getDamage());

				if (!stack.hasTagCompound())
				{
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setTag("SoulShardTag", tileEntity.getSoulShardStack().serializeNBT());
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntityInWorld(item);
			}
			world.removeTileEntity(pos);
		}
	}

	@Override public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase player,
			ItemStack stack)
	{
		super.onBlockPlacedBy(world, blockPos, state, player, stack);
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SoulShardTag") && !world.isRemote)
		{
			SoulCageTileEntity tileEntity = (SoulCageTileEntity) world.getTileEntity(blockPos);
			tileEntity.setSoulShard(
					ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("SoulShardTag")));
		}
	}

	@Override protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, METADATA);
	}

	@Override public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list)
	{
		list.add(new ItemStack(item, 1, 0));
	}

	@Override public IBlockState getStateFromMeta(int damage)
	{
		return getDefaultState().withProperty(METADATA, EnumType.byMetadata(damage));
	}

	@Override public List<ItemStack> getDrops(IBlockAccess blockAccess, BlockPos blockPos, IBlockState blockState,
			int p_getDrops_4_)
	{
		int damage = ((EnumType) blockState.getValue(METADATA)).getDamage();

		if (EnumType.UNBOUND_SOULCAGE.getDamage() == damage)
			return super.getDrops(blockAccess, blockPos, blockState, p_getDrops_4_);

		return new ArrayList<ItemStack>();
	}

	@Override public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}

	@Override public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override public int getMetaFromState(IBlockState state)
	{
		return ((EnumType) state.getValue(METADATA)).getDamage();
	}

	@Override public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override public String getSpecialName(ItemStack stack)
	{
		int damage = stack.getItemDamage();
		for (EnumType type : EnumType.META_LOOKUP)
		{
			if (type.getDamage() == damage)
				return type.name;
		}
		return EnumType.UNBOUND_SOULCAGE.getName();
	}

	@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult result, World world, BlockPos blockPos,
			EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, 0);
	}

	@Override public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player,
			EnumHand enumHand, ItemStack itemStack, EnumFacing enumFacing, float p_onBlockActivated_8_,
			float p_onBlockActivated_9_, float p_onBlockActivated_10_)
	{
		if (!world.isRemote)
		{

			TileEntity tile = world.getTileEntity(blockPos);

			if (tile == null || !(tile instanceof SoulCageTileEntity))
				return false;

			SoulCageTileEntity soulTile = (SoulCageTileEntity) tile;

			if (!player.isSneaking() && itemStack != null && itemStack.getItem() instanceof SoulShardItem
					&& soulTile.getSoulShard() == null)
			{

				if (Utils.isBound(itemStack))
				{

					int tier = Utils.getTier(itemStack);
					String entName = Utils.getEntityType(itemStack);

					if (tier == 0 || entName.isEmpty() || entName.equals("null"))
						return false;

					soulTile.setSoulShard(itemStack);

					world.setBlockState(blockPos, getStateFromMeta(EnumType.INACTIVE_SOULCAGE.getDamage()));

					if (!player.capabilities.isCreativeMode)
						itemStack.stackSize--;

					return true;
				}
			}

			if (itemStack == null && player.isSneaking())
			{
				if (soulTile.getSoulShard() != null)
				{
					Random rand = new Random();

					float dX = rand.nextFloat() * 0.8F + 0.1F;
					float dZ = rand.nextFloat() * 0.8F + 0.1F;

					EntityItem entityItem = new EntityItem(world, blockPos.getX() + dX, blockPos.getY() + 0.65, blockPos.getZ() + dZ,
							soulTile.getSoulShardStack());

					float factor = 0.05F;
					entityItem.motionX = rand.nextGaussian() * factor;
					entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
					entityItem.motionZ = rand.nextGaussian() * factor;

					world.spawnEntityInWorld(entityItem);

					world.setBlockState(blockPos, getStateFromMeta(EnumType.UNBOUND_SOULCAGE.getDamage()));
					soulTile.setSoulShard(null);
				}

				return true;
			}
		}
		return false;
	}

	@Override public TileEntity createNewTileEntity(World world, int i)
	{
		return new SoulCageTileEntity();
	}

	public enum EnumType implements IStringSerializable
	{

		UNBOUND_SOULCAGE(0, "unbound_soulcage"),
		INACTIVE_SOULCAGE(1, "inactive_soulcage"),
		ACTIVE_SOULCAGE(2, "active_soulcage");

		private int damage;
		private String name;
		private static EnumType[] META_LOOKUP;

		static
		{
			int i = 0;
			META_LOOKUP = new EnumType[values().length];
			for (EnumType type : values())
			{
				META_LOOKUP[i] = type;
				i++;
			}
		}

		EnumType(int id, String name)
		{
			this.damage = id;
			this.name = name;
		}

		@Override public String getName()
		{
			return name;
		}

		@Override public String toString()
		{
			return getName();
		}

		public int getDamage()
		{
			return damage;
		}

		public static EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}
	}
}

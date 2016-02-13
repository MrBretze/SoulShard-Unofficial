package fr.bretzel.soulshard.block;


import fr.bretzel.soulshard.SoulShard;
import fr.bretzel.soulshard.block.meta.IMetaBlockName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class SoulCage extends Block implements IMetaBlockName {

    public static final PropertyEnum METADATA = PropertyEnum.create("type", SoulCage.EnumType.class);

    public SoulCage(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(SoulShard.creativeTab);
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
        return type.getId();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public String getSpecialName(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage == 0)
            return EnumType.UNBOUND_SOULCAGE.getName();

        if (damage == 1)
            return EnumType.INACTIVE_SOULCAGE.getName();

        if (damage == 2)
            return EnumType.ACTIVE_SOULCAGE.getName();

        return EnumType.UNBOUND_SOULCAGE.getName();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    public static enum EnumType implements IStringSerializable {

        UNBOUND_SOULCAGE(0, "unbound_soulcage"),
        INACTIVE_SOULCAGE(1, "inactive_soulcage"),
        ACTIVE_SOULCAGE(2, "active_soulcage");

        private int id;
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
            this.id = id;
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

        public int getId() {
            return id;
        }

        public static EnumType byMetadata(int meta) {
            if(meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }
    }
}
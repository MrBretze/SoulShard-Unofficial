package fr.bretzel.soulshard.block;


import fr.bretzel.soulshard.SoulShard;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class SoulCage extends Block {


    public SoulCage(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);

        this.setUnlocalizedName(unlocalizedName);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setCreativeTab(SoulShard.creativeTab);
    }
}

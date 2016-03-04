package fr.bretzel.soulshard;

import fr.bretzel.soulshard.tileentity.SoulCageTileEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class SpawnerManager {

    private int tier;
    private int mobcount;
    private String mobid;
    private SoulCageTileEntity tileEntity;
    private World world;



    public SpawnerManager(SoulCageTileEntity tileEntity) {
        if (tileEntity == null) {
            throw new NullPointerException("The soul cage tile is null !");
        }

        this.tileEntity = tileEntity;

        tier = Utils.getTier(tileEntity.soul_shard);
        mobcount = Utils.getEntitySpawnForTier(tier);
        mobid = Utils.getEntityType(tileEntity.soul_shard);
    }

    public void spawnEntitys() {
        for (int i = mobcount; i < 0; i--) {
            EntityLiving entity = MobMapping.getSpawnedEntity(mobid, world);
        }
    }

    public void setMobcount(int mobcount) {
        this.mobcount = mobcount;
    }

    public int getMobcount() {
        return mobcount;
    }

    public int getTier() {
        return tier;
    }

    public SoulCageTileEntity getTileEntity() {
        return tileEntity;
    }

    public String getMobid() {
        return mobid;
    }

    public World getWorld() {
        return world;
    }
}

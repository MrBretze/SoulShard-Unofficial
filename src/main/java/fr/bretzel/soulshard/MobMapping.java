package fr.bretzel.soulshard;

import fr.bretzel.soulshard.config.MobConfig;
import fr.bretzel.soulshard.registry.CommonRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MobMapping {

    private World world;

    private ArrayList<String> blackList = new ArrayList<String>();
    private ArrayList<String> entityList = new ArrayList<String>();

    private MobConfig config;

    private static boolean loaded = false;

    public MobMapping(World world) {
        if (!isLoaded()) {
            this.world = world;

            loadDefaultBlackList();
            loadEntityList();

            config = new MobConfig(new File(CommonRegistry.configDirectory, "Mob.cfg"));
            config.getConfiguration().load();
            loadConfig();

            loaded = true;
        }
    }

    public void addMobBlackListed(String entityID) {
        if (EntityList.isStringValidEntityName(entityID)) {
            blackList.add(entityID);
        }
    }

    public boolean isMobBlackListed(String name) {
        return blackList.contains(name);
    }

    public boolean isMobBlackListed(EntityLiving entityLiving) {
        return isMobBlackListed(EntityList.getEntityString(entityLiving));
    }

    public static boolean isLoaded() {
        return loaded;
    }

    private void loadEntityList() {

        for (Map.Entry<Class<? extends Entity>, String> entry : EntityList.classToStringMapping.entrySet()) {
            if (entityList.contains(entry.getValue())) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": already mapped.");
            } else if (IBossDisplayData.class.isAssignableFrom(entry.getKey())) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": detected as boss.");
                addMobBlackListed(entry.getValue());
            } else if (EntityLiving.class.isAssignableFrom(entry.getKey())) {
                if (SoulShard.debug) {
                    SoulShard.soulLog.info("SoulShard: Mapped new entity " + entry.getValue());
                }
                entityList.add(entry.getValue());
            }
        }

        entityList.add("Wither Skeleton");

        DecimalFormat format = new DecimalFormat("00");

        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("=================== SOUL SHARD ====================");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("============== TOTAL ENTITY MAPPED: " + entityList.size() + " ============");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("============ BLACKLISTED ENTITY: " + format.format(blackList.size()) + " ===============");
        SoulShard.soulLog.info("===================================================");
    }

    private void loadConfig() {
        Configuration configuration = config.getConfiguration();

        try {

            Iterator<String> mobName = entityList.iterator();

            while (mobName.hasNext()) {

                String name = mobName.next();
                boolean value = configuration.get("Entity Blacklist", name, false).getBoolean(false);

                if (value) {
                    blackList.add(name);
                    SoulShard.soulLog.info("SoulShard: Added " + name + " to the black list !");
                }
            }

        } catch (Exception e) {
            SoulShard.soulLog.fatal("SoulShard: Had a problem loading it's configuration files.");
        } finally {
            config.setConfiguration(configuration);
            config.getConfiguration().save();
        }
    }

    private void loadDefaultBlackList() {
        blackList.add("Giant");
        blackList.add("Monster");
    }

    public World getWorld() {
        return world;
    }
}

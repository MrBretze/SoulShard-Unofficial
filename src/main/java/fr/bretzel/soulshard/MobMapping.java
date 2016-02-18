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
import java.util.ArrayList;
import java.util.HashMap;
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

    public boolean isMobBlackListed(String name) {
        return blackList.contains(name);
    }

    public boolean isMobBlackListed(EntityLiving entityLiving) {
        return entityLiving.hasCustomName() ? isMobBlackListed(EntityList.getEntityString(entityLiving)) : isMobBlackListed(entityLiving.getName());
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public String getEntityType(EntityLiving entityLiving) {
        String name = EntityList.getEntityString(entityLiving);
        if (entityList.contains(name))
            return name;
        else {
            name = (String) ((HashMap) EntityList.classToStringMapping).get(entityLiving.getClass());
            SoulShard.soulLog.info("New EntityType is detected " + name + ".");
            return name;
        }
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

    private void loadEntityList() {

        for (Map.Entry<Class<? extends Entity>, String> entry : EntityList.classToStringMapping.entrySet()) {
            if (entityList.contains(entry.getValue())) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": already mapped.");
            } else if (IBossDisplayData.class.isAssignableFrom(entry.getKey())) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": detected as boss.");
                blackList.add(entry.getValue());
            } else if (EntityLiving.class.isAssignableFrom(entry.getKey())) {
                if (SoulShard.debug) {
                    SoulShard.soulLog.info("SoulShard: Mapped new entity " + entry.getValue());
                }
                entityList.add(entry.getValue());
            }
        }

        entityList.add("Wither Skeleton");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("=================== SOUL SHARD ====================");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("============== TOTAL ENTITY MAPPED: " + entityList.size() + " ============");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("============= BLACKLISTED ENTITY: " + blackList.size() + " =================");
        SoulShard.soulLog.info("===================================================");
    }

    private void loadDefaultBlackList() {
        blackList.add("Giant");
        blackList.add("Monster");
    }

    public World getWorld() {
        return world;
    }
}

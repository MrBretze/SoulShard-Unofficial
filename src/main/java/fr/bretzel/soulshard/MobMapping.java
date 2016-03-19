package fr.bretzel.soulshard;

import fr.bretzel.soulshard.config.MobConfig;
import fr.bretzel.soulshard.registry.CommonRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
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

    private void loadEntityList() {
        for (Map.Entry<Class<? extends Entity>, String> entry : EntityList.classToStringMapping.entrySet()) {
            Entity entity = createEntityByName(entry.getValue(), world);
            if (entity == null) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": Entity is null");
            } else if (entityList.contains(entry.getValue())) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": already mapped.");
            } else if (!entity.isNonBoss()) {
                SoulShard.soulLog.info("SoulShard: Skipping mapping for " + entry.getValue() + ": detected as boss.");
                addMobBlackListed(entry.getValue());
            } else if (EntityLiving.class.isAssignableFrom(entry.getKey())) {
                entityList.add(entry.getValue());
            }
        }

        entityList.add("Wither Skeleton");

        DecimalFormat format = new DecimalFormat("00");

        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("=================== SOUL SHARD ====================");
        SoulShard.soulLog.info("===================================================");
        SoulShard.soulLog.info("============== TOTAL ENTITY MAPPED: " + format.format(entityList.size()) + " ============");
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
        addMobBlackListed("Giant");
        addMobBlackListed("Monster");
        addMobBlackListed("Mob");
    }

    public void addMobBlackListed(String entityID) {
        if (EntityList.isStringValidEntityName(entityID) && !blackList.contains(entityID)) {
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

    public static Entity createEntityByName(String p_createEntityByName_0_, World p_createEntityByName_1_) {
        Entity entity = null;

        Class<Entity> exception = (Class<Entity>) EntityList.stringToClassMapping.get(p_createEntityByName_0_);
        if (exception != null) {
            try {
                entity = (Entity) exception.getConstructor(new Class[]{World.class}).newInstance(p_createEntityByName_1_);
            } catch (Exception e) {
            }
        }


        return entity;
    }
}

package fr.bretzel.soulshard;

import fr.bretzel.soulshard.config.MobConfig;
import fr.bretzel.soulshard.registry.CommonRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MobMapping {

    private World world;

    private ArrayList<String> blackList = new ArrayList<String>();
    private ArrayList<String> entityList = new ArrayList<String>();
    private ArrayList<String> whitelist = new ArrayList<String>();

    private MobConfig config;

    public MobMapping(World world) {
        this.world = world;

        loadDefaultBlackList();
        loadEntityList();

        config = new MobConfig(new File(CommonRegistry.configDirectory, "Mob.cfg"));
        config.load();
        loadConfig();
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
            SoulShard.soulLog.fatal("Soul-Shards 3 had a problem loading it's configuration files.");
        } finally {
            config.setConfiguration(configuration);
            config.reload();
            config.getConfiguration().save();
        }
    }

    private void loadEntityList() {
        Map<Class<? extends Entity>, String> map = EntityList.classToStringMapping;
        Iterator<Class<? extends Entity>> iter = map.keySet().iterator();

        while (iter.hasNext()) {
            Class ent = iter.next();
            String entName = map.get(ent);

            if (entName.equals("Monster") || entName.equals("Mob")) continue;

            Entity entity = createEntityFromName(ent, entName);

            if (entity instanceof EntityLiving) {
                SoulShard.soulLog.info("SoulShard: Loaded mob: " + entName);

                EntityLiving living = (EntityLiving) entity;
                String name = living.getCommandSenderEntity().getName();

                if (!blackList.contains(name)) {
                    entityList.add(name);
                }
            }
        }
    }

    /**
     * SoulShard Reborn
     */
    private void loadDefaultBlackList() {
        blackList.add("EnderDragon");
        blackList.add("Giant");
        blackList.add("Monster");
        blackList.add("WitherBoss");
    }


    /**
     * SoulShard Reborn
     */
    private Entity createEntityFromName(Class entClass, String name) {
        if (!EntityLiving.class.isAssignableFrom(entClass)) return null;

        Entity entity = null;

        try {

            Constructor c = entClass.getConstructor(World.class);
            entity = (EntityLiving) c.newInstance(getWorld());

        } catch (Exception e) {
            SoulShard.soulLog.fatal("SoulShard: Skipping entity mapping for: " + name);
            SoulShard.soulLog.fatal("Please report this to dev: " + e.toString());
        }
        return entity;
    }

    public World getWorld() {
        return world;
    }
}

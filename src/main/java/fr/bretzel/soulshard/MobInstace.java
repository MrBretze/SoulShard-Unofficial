package fr.bretzel.soulshard;

import fr.bretzel.soulshard.api.IMobSpawnable;

import net.minecraft.entity.EntityLiving;

import java.util.HashMap;

public class MobInstace {

    private static HashMap<String, IMobSpawnable> sToM = new HashMap<String, IMobSpawnable>();

    public static void registerMob(String entityid, IMobSpawnable spawnable) {
        if (entityid.isEmpty() || entityid == null || spawnable == null) {
            throw new NullPointerException();
        }

        sToM.put(entityid, spawnable);
    }

}

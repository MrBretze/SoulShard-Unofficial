package fr.bretzel.soulshard.config;



import fr.bretzel.soulshard.SoulShard;

import java.io.File;

public class SoulShardConfig extends IConfig {

    public static boolean onlyVanillaSpawn = true;
    public static boolean requireOwnerOnline = false;
    public static int maxNumSpawns = 85;
    public static int soulStealerWeight = 5;
    public static int soulStealerID = 85;

    /*public int coolDown[] = new int[5];
    public int numOfMob[] = new int[5];
    public int killOfMob[] = new int[5];

    public boolean enableRedstone[] = new boolean[5];
    public boolean needPlayer[] = new boolean[5];
    public boolean checkLight[] = new boolean[5];
    public boolean greatWorld[] = new boolean[5];*/

    public SoulShardConfig(File file) {
        super(file);
        SoulShard.soulLog.info("Soul Shard has successful loaded " + file.getName());
    }

    public void load() {
        getConfiguration().load();
        requireOwnerOnline = getConfiguration().getBoolean("OwnerIsOnline", "General", false, "Soul Cage if owner is online to work");
        
        if (getConfiguration().hasChanged())
            getConfiguration().save();
    }

}

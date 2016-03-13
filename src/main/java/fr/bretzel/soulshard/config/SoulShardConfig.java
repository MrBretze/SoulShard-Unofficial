package fr.bretzel.soulshard.config;



import fr.bretzel.soulshard.SoulShard;

import java.io.File;

public class SoulShardConfig extends IConfig {

    public static int MAX_MOB_SPAWN = 85;
    public static int soulStealerWeight = 5;
    public static int soulStealerID = 85;

    public SoulShardConfig(File file) {
        super(file);
        SoulShard.soulLog.info("Soul Shard has successful loaded " + file.getName());
    }

    public void load() {
        getConfiguration().load();

        if (getConfiguration().hasChanged())
            getConfiguration().save();
    }

    private void validate() {

    }

}

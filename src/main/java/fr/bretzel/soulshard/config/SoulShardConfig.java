package fr.bretzel.soulshard.config;



import fr.bretzel.soulshard.SoulShard;

import java.io.File;

public class SoulShardConfig extends IConfig{

    public boolean onlyVanillaSpawn = true;
    public boolean requireOwnerOnline = false;
    public int maxNumSpawns = 85;
    public int soulStealerWeight = 5;
    public int soulStealerID = 85;

    public SoulShardConfig(File file) {
        super(file);
        SoulShard.soulLog.info("Soul Shard has successful loaded " + file.getName());
    }

    @Override
    public void load() {
        super.load();
        try {
            getConfiguration().load();

        } catch (Exception e) {
            SoulShard.soulLog.catching(e);
        } finally {
            if (getConfiguration().hasChanged())
                getConfiguration().save();
        }
    }
}
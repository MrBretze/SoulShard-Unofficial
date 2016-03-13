package fr.bretzel.soulshard;

public class TierUtils {

    private static boolean[] NEED_PLAYER = {true, true, false, false, false};
    private static boolean[] NEED_REDSTONE = {false, false, true, true, true};
    private static boolean[] CHECK_LIGHT = {true, true, true, true, false};

    private static int[] MOB_KILL = {0, 64, 128, 256, 512, 1024};
    private static int[] SPAWN_DELAY = {20, 13, 8, 5, 2};
    private static int[] NUM_SPAWNS = {2, 4, 4, 4, 6};

    public static void setNumSpawns(int tier, int value) {
        NUM_SPAWNS[getSpawnDelay(tier)] = value;
    }

    public static int getNumSpawns(int tier) {
        return NUM_SPAWNS[getValidTier(tier)];
    }

    public static void setPlayerChecks(int tier, boolean value) {
        NEED_PLAYER[getValidTier(tier)] = value;
    }

    public static boolean getPlayerCheck(int tier) {
        return NEED_PLAYER[getValidTier(tier)];
    }

    public static void setNeedRedstone(int tier, boolean value) {
        NEED_REDSTONE[getValidTier(tier)] = value;
    }

    public static boolean getNeedRedstone(int tier) {
        return NEED_REDSTONE[getValidTier(tier)];
    }

    public static void setSpawnDelay(int tier, int spawnDelay) {
        SPAWN_DELAY[getValidTier(tier)] = spawnDelay;
    }

    public static int getSpawnDelay(int tier) {
        return SPAWN_DELAY[getValidTier(tier)];
    }

    public static void setCheckLight(int tier, boolean value) {
        CHECK_LIGHT[getValidTier(tier)] = value;
    }

    public static boolean getCheckLight(int tier) {
        return CHECK_LIGHT[getValidTier(tier)];
    }

    public static int getMobKill(int tier) {
        return MOB_KILL[getValidTier(tier)];
    }

    public static void setMobKill(int tier, int value) {
        int[] tmpMob = MOB_KILL;

        tmpMob[getValidTier(tier)] = value;

        for (int i = 0; i < 5; i++) {
            if (tmpMob[i] > tmpMob[i + 1]) {
                SoulShard.soulLog.error("The config for tier: " + i + " the kill mob is superior of the tier: " + i + 1);
                return;
            }
        }

        MOB_KILL = tmpMob;
    }

    public static int getValidTier(int tier) {
        if (tier >= 5) {
            return 4;
        } else if (tier == -1) {
            return 0;
        } else {
            return tier;
        }
    }
}

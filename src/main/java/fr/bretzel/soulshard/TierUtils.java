package fr.bretzel.soulshard;

public class TierUtils
{

	private static boolean[] NEED_PLAYER = { true, true, false, false, false };
	private static boolean[] CHECKS_WORLD = { true, true, true, false, false };

	public static void setPlayerChecks(int tier, boolean value)
	{
		NEED_PLAYER[getValidTier(tier)] = value;
	}

	public static boolean getPlayerCheck(int tier)
	{
		return NEED_PLAYER[getValidTier(tier)];
	}

	public static void setWorldChecks(int tier, boolean value)
	{
		CHECKS_WORLD[getValidTier(tier)] = value;
	}

	public static boolean getWorldChecks(int tier)
	{
		return CHECKS_WORLD[getValidTier(tier)];
	}

	private static int getValidTier(int tier)
	{
		if (tier == 5)
		{
			return 4;
		} else if (tier == -1)
		{
			return 0;
		} else
		{
			return tier;
		}
	}
}

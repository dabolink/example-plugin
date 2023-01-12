package com.dabolink;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("xprelicsound")
public interface XPRelicSoundConfig extends Config
{
	@ConfigItem(
		keyName = "relic sound chance",
		name = "1/x chance (per xp)",
		description = "1 in x chance per xp for relic sound to play"
	)
	default int procChance()
	{
		return 100000;
	}

	@ConfigItem(
		keyName = "include maxed skills",
		name = "include maxed skills",
		description = "if true, roll even if the skill is maxed"
	)
	default boolean includeMaxedSkills()
	{
		return true;
	}


}

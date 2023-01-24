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
		description = "1 in x chance per xp for relic sound to play."
	)
	default int procChance()
	{
		return 100000;
	}

	@ConfigItem(
		keyName = "include maxed skills",
		name = "Include maxed skills.",
		description = "If true, roll even if the skill is maxed."
	)
	default boolean includeMaxedSkills()
	{
		return true;
	}

	@ConfigItem(
		keyName = "relic sound id",
		name = "Relic sound ID.",
		description = "Sound ID to play if roll is successful."
	)
	default int relicSound() { return 4212; }

	@ConfigItem(
		keyName = "relic chat message",
		name = "Include chat message.",
		description = "If true, a chat message will be written if roll is successful."
	)
	default boolean includeChatMessage() { return true; }
}

package com.dabolink;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "XP Relic Sound"
)
public class XPRelicSoundPlugin extends Plugin
{
	private static final int RELIC_SOUND_ID = 4212;

	private final Map<Skill, Integer> skillXPMap = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private XPRelicSoundConfig config;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		Skill skill = statChanged.getSkill();
		int experience = statChanged.getXp();

		if (!skillXPMap.containsKey(skill))
		{
			skillXPMap.put(skill, experience);
			return;
		}

		if (!config.includeMaxedSkills() && statChanged.getLevel() >= 99)
		{
			skillXPMap.put(skill, experience);
			return;
		}

		if (rollChance(experience - skillXPMap.get(skill)))
		{
			client.playSoundEffect(RELIC_SOUND_ID);
		}

		skillXPMap.put(skill, experience);
	}

	private boolean rollChance(int experienceGained)
	{
		double chance = config.procChance();
		if (chance == 0)
		{
			return false;
		}

		double procChance = 1 - Math.pow((chance - 1) / chance, experienceGained);
		double roll = Math.random();

		return roll <= procChance;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			skillXPMap.clear();
		}
	}

	@Provides
	XPRelicSoundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(XPRelicSoundConfig.class);
	}
}

package com.dabolink;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
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
	private static final String RELIC_CHAT_MESSAGE = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append("You have a sad feeling like you would have got a relic.")
				.build();

	private final Map<Skill, Integer> skillXPMap = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

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
			procRelicEffect();
		}

		skillXPMap.put(skill, experience);
	}

	private void procRelicEffect() {
		if (config.includeChatMessage()) {
			chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.CONSOLE)
					.runeLiteFormattedMessage(RELIC_CHAT_MESSAGE)
					.build());
		}
		client.playSoundEffect(config.relicSound());
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

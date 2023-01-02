package com.dabolink;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class XPRelicSoundPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(XPRelicSoundPlugin.class);
		RuneLite.main(args);
	}
}
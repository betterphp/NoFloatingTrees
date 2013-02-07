package uk.co.jacekk.bukkit.nofloatingtrees;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.v9.config.PluginConfigKey;

public class Config {
	
	public static final PluginConfigKey USE_LOGBLOCK			= new PluginConfigKey("use-logblock",		true);
	public static final PluginConfigKey IGNORE_WORLDS			= new PluginConfigKey("ignore-worlds",		Arrays.asList("world_nether", "world_the_end"));
	public static final PluginConfigKey DECAY_FREQUENCY			= new PluginConfigKey("decay.frequency",	2);
	public static final PluginConfigKey DECAY_WAIT_TIME			= new PluginConfigKey("decay.wait-time",	40);
	public static final PluginConfigKey DECAY_CHANCE			= new PluginConfigKey("decay.chance",		15);
	public static final PluginConfigKey DECAY_DROP_CHANCE		= new PluginConfigKey("decay.drop-chance",	15);
	
}

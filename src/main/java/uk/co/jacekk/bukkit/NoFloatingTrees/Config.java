package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.v1.config.PluginConfigKey;

public enum Config implements PluginConfigKey {
	
	USE_LOGBLOCK(		"use-logblock",			true),
	IGNORE_WORLDS(		"ignore-worlds",		Arrays.asList("world_nether", "world_the_end")),
	DECAY_FREQUENCY(	"decay.frequency",		2),
	DECAY_WAIT_TIME(	"decay.wait-time",		40),
	DECAY_CHANCE(		"decay.chance",			15),
	DECAY_DROP_CHANCE(	"decay.drop-chance",	15);
	
	private String key;
	private Object defaultValue;
	
	private Config(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public Object getDefault(){
		return this.defaultValue;
	}
	
}

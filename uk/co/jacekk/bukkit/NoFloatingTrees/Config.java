package uk.co.jacekk.bukkit.NoFloatingTrees;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfigKey;

public enum Config implements PluginConfigKey {
	
	USE_LOGBLOCK(		"use-logblock",		true);
	
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

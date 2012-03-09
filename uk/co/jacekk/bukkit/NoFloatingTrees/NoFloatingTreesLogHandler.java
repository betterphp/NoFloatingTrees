package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;

public class NoFloatingTreesLogHandler {
	
	private NoFloatingTrees plugin;
	
	protected Logger log;
	
	public NoFloatingTreesLogHandler(NoFloatingTrees instance, String logger){
		this.plugin = instance;
		
		this.log = Logger.getLogger(logger);
	}
	
	private String buildString(String message){
		PluginDescriptionFile pdFile = plugin.getDescription();
		
		return pdFile.getName() + " " + pdFile.getVersion() + ": " + message;
	}
	
	public void info(String message){
		this.log.info(this.buildString(message));
	}
	
	public void warn(String message){
		this.log.warning(this.buildString(message));
	}
	
}

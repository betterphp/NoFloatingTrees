package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.jacekk.bukkit.NoFloatingTrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.LocationStore;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.NoFloatingTreesConfig;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.LogHandler;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends JavaPlugin {
	
	public NoFloatingTreesConfig config;
	public LogHandler log;
	public Consumer lb;
	public Server server;
	public PluginManager manager;
	
	public LocationStore locations;
	
	public void onEnable(){
		this.server = this.getServer();
		this.manager = this.server.getPluginManager();
		
		String pluginDir = this.getDataFolder().getAbsolutePath();
		(new File(pluginDir)).mkdirs();
		
		this.config = new NoFloatingTreesConfig(new File(pluginDir + File.separator + "config.yml"), this);
		this.locations = new LocationStore(new File(pluginDir + File.separator + "tree-locations.bin"));
		
		this.log = new LogHandler(this, "Minecraft");
		
		this.server.getScheduler().scheduleSyncRepeatingTask(this, new LogDecayTask(this), 20, 20);
		
		if (this.manager.isPluginEnabled("LogBlock") && this.config.getBoolean("use-logblock")){
			this.lb = ((LogBlock) this.manager.getPlugin("LogBlock")).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		manager.registerEvents(new TreeBreakListener(this), this);
		
		this.log.info("Enabled.");
	}
	
	public void onDisable(){
		this.log.info("Disabled.");
	}
	
}

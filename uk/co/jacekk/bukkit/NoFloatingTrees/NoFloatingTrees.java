package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;

import org.bukkit.Location;

import uk.co.jacekk.bukkit.NoFloatingTrees.commands.NftDebugExecutor;
import uk.co.jacekk.bukkit.NoFloatingTrees.commands.NftPurgeExecutor;
import uk.co.jacekk.bukkit.NoFloatingTrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.BlockLocationStore;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.LocationStore;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.NoFloatingTreesConfig;
import uk.co.jacekk.bukkit.baseplugin.BasePlugin;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends BasePlugin {
	
	public NoFloatingTreesConfig config;
	
	public Consumer lb;
	
	public BlockLocationStore blockLocations;
	
	public void onEnable(){
		String pluginDir = this.getDataFolder().getAbsolutePath();
		(new File(pluginDir)).mkdirs();
		
		this.config = new NoFloatingTreesConfig(new File(pluginDir + File.separator + "config.yml"), this);
		this.blockLocations = new BlockLocationStore(new File(pluginDir + File.separator + "log-locations.bin"));
		this.blockLocations.load();
		
		if (this.pluginManager.isPluginEnabled("LogBlock") && this.config.getBoolean("use-logblock")){
			this.lb = ((LogBlock) this.pluginManager.getPlugin("LogBlock")).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		this.scheduler.scheduleSyncRepeatingTask(this, new LogDecayTask(this), 45, 40);
		
		this.pluginManager.registerEvents(new TreeBreakListener(this), this);
		
		this.getCommand("nftpurge").setExecutor(new NftPurgeExecutor(this));
		this.getCommand("nftdebug").setExecutor(new NftDebugExecutor(this));
		
		this.log.info("Enabled.");
	}
	
	public void onDisable(){
		this.blockLocations.save();
		
		this.log.info("Disabled.");
	}
	
}

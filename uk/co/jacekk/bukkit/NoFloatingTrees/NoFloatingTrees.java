package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;

import uk.co.jacekk.bukkit.NoFloatingTrees.commands.NftDebugExecutor;
import uk.co.jacekk.bukkit.NoFloatingTrees.commands.NftPurgeExecutor;
import uk.co.jacekk.bukkit.NoFloatingTrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.BlockLocationStore;
import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends BasePlugin {
	
	public Consumer lb;
	
	public BlockLocationStore blockLocations;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		this.blockLocations = new BlockLocationStore(new File(this.baseDirPath + File.separator + "log-locations.bin"));
		this.blockLocations.load();
		
		if (this.pluginManager.isPluginEnabled("LogBlock") && this.config.getBoolean(Config.USE_LOGBLOCK)){
			this.lb = ((LogBlock) this.pluginManager.getPlugin("LogBlock")).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		this.scheduler.scheduleSyncRepeatingTask(this, new LogDecayTask(this), 45, 40);
		
		this.pluginManager.registerEvents(new TreeBreakListener(this), this);
		
		this.getCommand("nftpurge").setExecutor(new NftPurgeExecutor(this));
		this.getCommand("nftdebug").setExecutor(new NftDebugExecutor(this));
	}
	
	public void onDisable(){
		this.blockLocations.save();
	}
	
}

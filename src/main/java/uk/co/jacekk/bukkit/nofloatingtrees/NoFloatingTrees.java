package uk.co.jacekk.bukkit.nofloatingtrees;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.block.Block;

import uk.co.jacekk.bukkit.baseplugin.v9.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v9.config.PluginConfig;
import uk.co.jacekk.bukkit.nofloatingtrees.commands.NftExecutor;
import uk.co.jacekk.bukkit.nofloatingtrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.DecayQueue;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends BasePlugin {
	
	public Consumer logblock;
	
	public DecayQueue decayQueue;
	private TreeBreakListener listener;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		
		this.decayQueue = new DecayQueue(this, new File(this.baseDirPath + File.separator + "block-locations.bin"));
		this.decayQueue.load();
		
		if (this.pluginManager.isPluginEnabled("LogBlock") && this.config.getBoolean(Config.USE_LOGBLOCK)){
			this.logblock = ((LogBlock) this.pluginManager.getPlugin("LogBlock")).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		this.scheduler.scheduleSyncRepeatingTask(this, new LogDecayTask(this), 10, 20 * this.config.getInt(Config.DECAY_FREQUENCY));
		
		this.listener = new TreeBreakListener(this);
		this.pluginManager.registerEvents(this.listener, this);
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new NftExecutor(this));
	}
	
	public void onDisable(){
		this.decayQueue.save();
	}
	
	/**
	 * Get tree of a block
	 * 
	 * @param block The block to check for a tree
	 * @param force if true, ignore not being directly above dirt/grass, or not under another log, or if topmost block is not leaves 
	 * @return {@link ArrayList<Block>} of tree log blocks, or null
	 */
	public ArrayList<Block> getTree(Block block, boolean force){
		return this.listener.getTree(block, force);
	}
	
}

package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.jacekk.bukkit.NoFloatingTrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.NoFloatingTrees.listeners.WorldInitListener;
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
		manager.registerEvents(new WorldInitListener(this), this);
		
		this.log.info("Enabled.");
	}
	
	public void onDisable(){
		this.log.info("Disabled.");
	}
	
	public boolean looksLikeTrunk(Block block){
		UUID worldId = block.getWorld().getUID();
		ArrayList<int[]> coordList = this.coordList.get(worldId);
		
		if (coordList == null){
			coordList = new ArrayList<int[]>();
			this.coordList.put(worldId, coordList);
		}
		
		int[] coords = new int[2];
		coords[0] = block.getX();
		coords[1] = block.getZ();
		
		if (block.getType() != Material.LOG) return false;
		
		for (int[] coord : coordList){
			if (coord[0] == coords[0] && coord[1] == coords[1]){
				return false;
			}
		}
		
		if (Arrays.asList(Material.DIRT, Material.GRASS, Material.LOG, Material.AIR).contains(block.getRelative(BlockFace.DOWN).getType()) == false) return false;
		
		if (block.getRelative(BlockFace.UP).getType() != Material.LOG) return false;
		
		Block highest = block.getWorld().getHighestBlockAt(coords[0], coords[1]);
		Block top = (highest.getType() == Material.AIR) ? highest.getRelative(BlockFace.DOWN) : highest;
				
		if (top.getType() != Material.LEAVES) return false;
		
		return true;
	}
	
}

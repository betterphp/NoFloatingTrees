package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends JavaPlugin {
	
	protected NoFloatingTreesConfig config;
	protected NoFloatingTreesLogHandler log;
	protected Consumer lb;
	
	protected HashMap<UUID, ArrayList<int[]>> coordList;
	
	protected NoFloatingTreesRemoveAllTask removeAllTask;
	
	public void onEnable(){
		this.config = new NoFloatingTreesConfig(new File(this.getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
		this.log = new NoFloatingTreesLogHandler(this, "Minecraft");
		
		this.coordList = new HashMap<UUID, ArrayList<int[]>>();
		
		this.removeAllTask = new NoFloatingTreesRemoveAllTask(this);
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.removeAllTask, 0, this.config.getInt("remove-freq") * 60 * 20);
		
		Plugin logBlock = this.getServer().getPluginManager().getPlugin("LogBlock");
		
		if (logBlock != null && this.config.getBoolean("use-logblock")){
			this.lb = ((LogBlock) logBlock).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		this.getCommand("nftpurge").setExecutor(new NoFloatingTreesCommandExecutor(this));
		
		PluginManager manager = this.getServer().getPluginManager();
		
		manager.registerEvents(new NoFloatingTreesBlockListener(this), this);
		manager.registerEvents(new NoFloatingTreesEntityListener(this), this);
		manager.registerEvents(new NoFloatingTreesWorldListener(this), this);
		
		this.log.info("Enabled.");
	}
	
	public void onDisable(){
		this.log.info("Disabled.");
		
		this.log = null;
		this.lb = null;
		
		this.coordList = null;
		
		this.removeAllTask = null;
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

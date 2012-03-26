package uk.co.jacekk.bukkit.NoFloatingTrees;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TreeCheckTask implements Runnable {
	
	private NoFloatingTrees plugin;
	
	public TreeCheckTask(NoFloatingTrees plugin){
		this.plugin = plugin;
	}
	
	private boolean isTreeBlock(Block block){
		Material type = block.getType();
		
		return (type == Material.LEAVES || type == Material.LOG);
	}
	
	public void run(){
		plugin.log.info("Tree Queue Size: " + plugin.treeLocations.size());
		
		World world;
		Integer yMax, x, y, z;
		
		for (Location location : plugin.treeLocations.getAll()){
			if (location.getChunk().isLoaded()){
				world = location.getWorld();
				
				x = location.getBlockX();
				yMax = world.getHighestBlockYAt(location);
				z = location.getBlockZ();
				
				if (world.getBlockTypeIdAt(x, yMax, z) == Material.AIR.getId()){
					--yMax;
				}
				
				if (this.isTreeBlock(world.getBlockAt(x, yMax, z)) == false){
					continue;
				}
				
				
			}
		}
	}
	
}

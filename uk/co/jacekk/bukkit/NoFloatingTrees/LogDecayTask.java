package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class LogDecayTask implements Runnable {
	
	private NoFloatingTrees plugin;
	private Random rand;
	
	public LogDecayTask(NoFloatingTrees instance){
		this.plugin = instance;
		this.rand = new Random();
	}
	
	public void run(){
		plugin.log.info("Block Queue Size: " + plugin.blockLocations.size());
		
		Block block;
		
		for (Location location : plugin.blockLocations.getAll()){
			if (location.getChunk().isLoaded()){
				block = location.getBlock();
				
				if (rand.nextInt(100) < 20){
					block.setType(Material.AIR);
					
					if (rand.nextBoolean()){
						location.getWorld().dropItemNaturally(location, new ItemStack(block.getTypeId(), 1));
					}
					
					plugin.blockLocations.remove(location);
					
					if (plugin.lb != null){
						plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
					}
				}
			}
		}
	}

}

package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LogDecayTask implements Runnable {
	
	private NoFloatingTrees plugin;
	private Random rand;
	
	public LogDecayTask(NoFloatingTrees instance){
		this.plugin = instance;
		this.rand = new Random();
	}
	
	public void run(){
		Block block;
		Material type;
		ItemStack drop;
		
		locations: for (Location location : plugin.blockLocations.getAll()){
			if (location.getChunk().isLoaded() && rand.nextInt(100) < 15){
				block = location.getBlock();
				type = block.getType();
				
				for (Player player : location.getWorld().getPlayers()){
					if (player.getLocation().distance(location) < 40){
						continue locations;
					}
				}
				
				if (type == Material.LOG){
					drop = new ItemStack(type, 1, (short) 0, block.getData()); 
					
					block.setType(Material.AIR);
					
					if (rand.nextInt(100) < 15){
						location.getWorld().dropItemNaturally(location, drop);
					}
					
					if (plugin.lb != null){
						plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
					}
				}
				
				plugin.blockLocations.remove(location);
			}
		}
	}

}

package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.NoFloatingTrees.util.BlockLocationStorable;

public class LogDecayTask implements Runnable {
	
	private NoFloatingTrees plugin;
	private Random rand;
	
	public LogDecayTask(NoFloatingTrees instance){
		this.plugin = instance;
		this.rand = new Random();
	}
	
	public void run(){
		Block block;
		Location location;
		Material type;
		ItemStack drop;
		
		for (World world : plugin.server.getWorlds()){
			for (Chunk chunk : world.getLoadedChunks()){
				locations: for (BlockLocationStorable blockLocation : plugin.blockLocations.getAll(chunk)){
					if (this.rand.nextInt(100) < 15){
						block = blockLocation.getBlock();
						location = block.getLocation();
						type = block.getType();
						
						for (Player player : blockLocation.getWorld().getPlayers()){
							if (player.getLocation().distance(block.getLocation()) < 40){
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
	}

}

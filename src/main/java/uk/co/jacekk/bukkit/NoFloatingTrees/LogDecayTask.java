package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.NoFloatingTrees.util.BlockLocationStorable;
import uk.co.jacekk.bukkit.baseplugin.v1.scheduler.BaseTask;

public class LogDecayTask extends BaseTask<NoFloatingTrees> {
	
	private Random rand;
	
	public LogDecayTask(NoFloatingTrees plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	public void run(){
		Block block;
		Location location;
		Material type;
		
		ArrayList<Location> remove = new ArrayList<Location>();
		
		for (World world : plugin.server.getWorlds()){
			if (plugin.config.getStringList(Config.IGNORE_WORLDS).contains(world.getName())){
				continue;
			}
			
			for (Chunk chunk : world.getLoadedChunks()){
				locations: for (BlockLocationStorable blockLocation : plugin.blockLocations.getAll(chunk)){
					block = blockLocation.getBlock();
					location = block.getLocation();
					type = block.getType();
					
					if (type != Material.LOG){
						remove.add(location);
					}else if (this.rand.nextInt(100) < 15){
						for (Player player : blockLocation.getWorld().getPlayers()){
							if (player.getLocation().distance(block.getLocation()) < 40){
								continue locations;
							}
						}
						
						if (rand.nextInt(100) < 15){
							location.getWorld().dropItemNaturally(location, new ItemStack(type, 1, (short) 0, block.getData()));
						}
						
						block.setType(Material.AIR);
						
						if (plugin.lb != null){
							plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
						}
						
						remove.add(location);
					}
				}
			}
		}
		
		plugin.blockLocations.removeAll(remove);
	}

}

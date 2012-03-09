package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class NoFloatingTreesEntityListener implements Listener {
	
	NoFloatingTrees plugin;
	
	public NoFloatingTreesEntityListener(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplode(EntityExplodeEvent event){
		if (event.isCancelled()) return;
		
		UUID worldId = event.getLocation().getWorld().getUID();
		ArrayList<int[]> coordList;
		
		if (plugin.coordList.containsKey(worldId)){
			coordList = plugin.coordList.get(worldId);
		}else{
			coordList = new ArrayList<int[]>();
		}
		
		for (Block block : event.blockList()){
			if (plugin.looksLikeTrunk(block)){
				int[] coords = new int[2];
				coords[0] = block.getX();
				coords[1] = block.getZ();
				
				coordList.add(coords);
			}
		}
		
		plugin.coordList.put(worldId, coordList);
	}
	
}

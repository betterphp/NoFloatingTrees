package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class NoFloatingTreesBlockListener implements Listener {
	
	NoFloatingTrees plugin;
	
	public NoFloatingTreesBlockListener(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event){
		if (event.isCancelled()) return;
		
		Block block = event.getBlock();
		UUID worldId = block.getWorld().getUID();
		ArrayList<int[]> coordList;
		
		if (plugin.coordList.containsKey(worldId)){
			coordList = plugin.coordList.get(worldId);
		}else{
			coordList = new ArrayList<int[]>();
		}
		
		if (plugin.looksLikeTrunk(block)){
			int[] coords = new int[2];
			coords[0] = block.getX();
			coords[1] = block.getZ();
			
			coordList.add(coords);
			
			plugin.coordList.put(worldId, coordList);
		}
	}

}

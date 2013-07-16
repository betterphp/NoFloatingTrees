package uk.co.jacekk.bukkit.nofloatingtrees.listeners;

import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;

public class TreeCreateListener extends BaseListener<NoFloatingTrees> {
	
	public TreeCreateListener(NoFloatingTrees plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onTreeGrow(StructureGrowEvent event){
		for (BlockState block : event.getBlocks()){
			plugin.decayQueue.removeBlock(block.getBlock());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		plugin.decayQueue.removeBlock(event.getBlock());
	}
	
}

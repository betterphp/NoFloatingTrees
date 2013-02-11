package uk.co.jacekk.bukkit.nofloatingtrees.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.nofloatingtrees.Config;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;

public class TreeBreakListener extends BaseListener<NoFloatingTrees> {
	
	public TreeBreakListener(NoFloatingTrees plugin){
		super(plugin);
	}
	
	public void processTreeBlockBreak(Block block){
		ArrayList<Block> tree = plugin.getTree(block, false);
		
		if (tree != null){
			for (Block log : tree){
				plugin.decayQueue.addBlock(log);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		
		if (plugin.config.getStringList(Config.IGNORE_WORLDS).contains(block.getWorld().getName())){
			return;
		}
		
		if (block.getType() == Material.LOG){
			this.processTreeBlockBreak(block);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event){
		if (plugin.config.getStringList(Config.IGNORE_WORLDS).contains(event.getLocation().getWorld().getName())){
			return;
		}
		
		for (Block block : event.blockList()){
			if (block.getType() == Material.LOG){
				this.processTreeBlockBreak(block);
			}
		}
	}

}

package uk.co.jacekk.bukkit.NoFloatingTrees.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;

public class TreeBreakListener implements Listener {
	
	NoFloatingTrees plugin;
	
	public TreeBreakListener(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	public boolean looksLikeTrunk(Block block){
		Material blockType = block.getType();
		Material aboveType = block.getRelative(BlockFace.UP).getType();
		Material belowType = block.getRelative(BlockFace.DOWN).getType();
		
		if (Arrays.asList(Material.LOG).contains(blockType) == false) return false;
		
		if (Arrays.asList(Material.DIRT, Material.GRASS, Material.LOG, Material.AIR).contains(belowType) == false) return false;
		
		if (Arrays.asList(Material.LOG).contains(aboveType) == false) return false;
		
		Block highest = block.getWorld().getHighestBlockAt(block.getX(), block.getZ());
		
		if (highest.getType() == Material.AIR){
			highest = highest.getRelative(BlockFace.DOWN);
		}
		
		if (Arrays.asList(Material.LEAVES).contains(highest.getType())) return false;
		
		return true;
	}
	
	private void processBlockBreak(Block block){
		Location location = block.getLocation();
		
		if (plugin.locations.contains(location) == false){
			plugin.locations.add(location);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		
		if (this.looksLikeTrunk(block)){
			this.processBlockBreak(block);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event){
		if (event.isCancelled()) return;
		
		for (Block block : event.blockList()){
			if (this.looksLikeTrunk(block)){
				this.processBlockBreak(block);
			}
		}
	}

}

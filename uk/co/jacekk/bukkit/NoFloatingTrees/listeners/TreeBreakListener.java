package uk.co.jacekk.bukkit.NoFloatingTrees.listeners;

import java.util.ArrayList;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.World;
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
	
	private Block[] getSurroundingBlocks(Block block){
		Block[] blocks = new Block[14];
		
		blocks[0] = block.getRelative(BlockFace.UP);
		blocks[1] = block.getRelative(BlockFace.DOWN);
		blocks[2] = block.getRelative(BlockFace.NORTH);
		blocks[3] = block.getRelative(BlockFace.SOUTH);
		blocks[4] = block.getRelative(BlockFace.EAST);
		blocks[5] = block.getRelative(BlockFace.WEST);
		blocks[6] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
		blocks[7] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
		blocks[8] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
		blocks[9] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
		blocks[10] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
		blocks[11] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
		blocks[12] = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
		blocks[13] = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
		
		return blocks;
	}
	
	private void processTreeBlockBreak(Block block){
		World world = block.getWorld();
		
		// Not part of the trunk ?
		if (block.getRelative(BlockFace.DOWN).getType() != Material.DIRT && block.getRelative(BlockFace.DOWN).getType() != Material.LOG){
			return;
		}
		
		if (block.getRelative(BlockFace.UP).getType() != Material.LOG){
			return;
		}
		
		// Part of a wall ?
		if (block.getRelative(BlockFace.NORTH).getType() == Material.LOG && block.getRelative(BlockFace.SOUTH).getType() == Material.LOG){
			return;
		}
		
		if (block.getRelative(BlockFace.EAST).getType() == Material.LOG && block.getRelative(BlockFace.WEST).getType() == Material.LOG){
			return;
		}
		
		int x = block.getX();
		int y;
		int z = block.getZ();
		
		for (y = block.getY(); world.getBlockTypeIdAt(x, y, z) != 0; ++y);
		
		// No leaf block a the top ?
		if (world.getBlockTypeIdAt(x, y - 1, z) != Material.LEAVES.getId()){
			return;
		}
		
		ArrayList<Block> logs = new ArrayList<Block>();
		Stack<Block> queue = new Stack<Block>();
		
		logs.add(block);
		queue.push(block);
		
		while (queue.isEmpty() == false){
			for (Block surrounding : this.getSurroundingBlocks(queue.pop())){
				if (surrounding.getType() == Material.LOG && logs.contains(surrounding) == false){
					logs.add(surrounding);
					queue.push(surrounding);
				}
			}
		}
		
		for (Block log : logs){
			plugin.blockLocations.add(log.getLocation());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		
		if (block.getType() == Material.LOG){
			this.processTreeBlockBreak(block);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event){
		for (Block block : event.blockList()){
			if (block.getType() == Material.LOG){
				this.processTreeBlockBreak(block);
			}
		}
	}

}

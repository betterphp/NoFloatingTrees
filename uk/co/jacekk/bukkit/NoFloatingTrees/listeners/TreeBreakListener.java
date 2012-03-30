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
		Block[] blocks = new Block[13];
		
		blocks[0] = block.getRelative(BlockFace.UP);
		blocks[1] = block.getRelative(BlockFace.NORTH);
		blocks[2] = block.getRelative(BlockFace.SOUTH);
		blocks[3] = block.getRelative(BlockFace.EAST);
		blocks[4] = block.getRelative(BlockFace.WEST);
		blocks[5] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
		blocks[6] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
		blocks[7] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
		blocks[8] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
		blocks[9] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
		blocks[10] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
		blocks[11] = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
		blocks[12] = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
		
		return blocks;
	}
	
	private void processTreeBlockBreak(Block block){
		World world = block.getWorld();
		
		// Not part of the trunk ?
		Material aboveType = block.getRelative(BlockFace.UP).getType();
		Material belowType = block.getRelative(BlockFace.DOWN).getType();
		
		if ((belowType != Material.DIRT && belowType != Material.LOG) || aboveType != Material.LOG){
			return;
		}
		
		// Left a floating tree and not part of a house ?
		Material northType = block.getRelative(BlockFace.NORTH).getType();
		Material southType = block.getRelative(BlockFace.SOUTH).getType();
		Material eastType = block.getRelative(BlockFace.EAST).getType();
		Material westType = block.getRelative(BlockFace.WEST).getType();
		
		if (northType == Material.LOG || southType == Material.LOG || eastType == Material.LOG || westType == Material.LOG){
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

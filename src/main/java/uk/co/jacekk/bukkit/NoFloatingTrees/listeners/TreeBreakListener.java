package uk.co.jacekk.bukkit.NoFloatingTrees.listeners;

import java.util.ArrayList;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import uk.co.jacekk.bukkit.NoFloatingTrees.Config;
import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;

public class TreeBreakListener extends BaseListener<NoFloatingTrees> {
	
	public TreeBreakListener(NoFloatingTrees plugin){
		super(plugin);
	}
	
	private Block[] getSurroundingBlocks(Block block){
		Block[] blocks = new Block[17];
		
		blocks[0] = block.getRelative(BlockFace.UP);
		blocks[1] = block.getRelative(BlockFace.NORTH);
		blocks[2] = block.getRelative(BlockFace.NORTH_EAST);
		blocks[3] = block.getRelative(BlockFace.NORTH_WEST);
		blocks[4] = block.getRelative(BlockFace.SOUTH);
		blocks[5] = block.getRelative(BlockFace.SOUTH_EAST);
		blocks[6] = block.getRelative(BlockFace.SOUTH_WEST);
		blocks[7] = block.getRelative(BlockFace.EAST);
		blocks[8] = block.getRelative(BlockFace.WEST);
		blocks[9] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
		blocks[10] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST);
		blocks[11] = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST);
		blocks[12] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
		blocks[13] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST);
		blocks[14] = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST);
		blocks[15] = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
		blocks[16] = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
		
		return blocks;
	}
	
	public void processTreeBlockBreak(Block block){
		ArrayList<Block> tree = getTree(block, false);
		
		if (tree != null){
			for (Block log : tree){
				plugin.decayQueue.addBlock(log);
			}
		}
	}
	
	public ArrayList<Block> getTree(Block block, boolean force){
		World world = block.getWorld();
		
		// Not part of the trunk ?
		Material aboveType = block.getRelative(BlockFace.UP).getType();
		Material belowType = block.getRelative(BlockFace.DOWN).getType();
		
		if (!force && ((belowType != Material.DIRT && belowType != Material.GRASS && belowType != Material.LOG) || aboveType != Material.LOG)){
			return null;
		}
		
		// Left a floating tree and not part of a house ?
		Material northType = block.getRelative(BlockFace.NORTH).getType();
		Material southType = block.getRelative(BlockFace.SOUTH).getType();
		Material eastType = block.getRelative(BlockFace.EAST).getType();
		Material westType = block.getRelative(BlockFace.WEST).getType();
		
		if (northType == Material.LOG || southType == Material.LOG || eastType == Material.LOG || westType == Material.LOG){
			return null;
		}
		
		int x = block.getX();
		int y;
		int z = block.getZ();
		
		for (y = block.getY(); world.getBlockTypeIdAt(x, y, z) != 0; ++y);
		
		if (!force){
			Integer topId = world.getBlockTypeIdAt(x, y - 1, z);
			
			if (topId == Material.SNOW.getId()){
				topId = world.getBlockTypeIdAt(x, y - 2, z);
			}
			
			// No leaf block a the top ?
			if (topId != Material.LEAVES.getId()){
				return null;
			}
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
		
		return logs;
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

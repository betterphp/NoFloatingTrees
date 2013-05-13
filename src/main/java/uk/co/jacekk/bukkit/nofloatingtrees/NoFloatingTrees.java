package uk.co.jacekk.bukkit.nofloatingtrees;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.nofloatingtrees.commands.NftExecutor;
import uk.co.jacekk.bukkit.nofloatingtrees.listeners.TreeBreakListener;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.DecayQueue;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

public class NoFloatingTrees extends BasePlugin {
	
	public Consumer logblock;
	
	public DecayQueue decayQueue;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		
		this.decayQueue = new DecayQueue(this, new File(this.baseDirPath + File.separator + "block-locations.bin"));
		this.decayQueue.load();
		
		if (this.pluginManager.isPluginEnabled("LogBlock") && this.config.getBoolean(Config.USE_LOGBLOCK)){
			this.logblock = ((LogBlock) this.pluginManager.getPlugin("LogBlock")).getConsumer();
			this.log.info("LogBlock found removed blocks will be logged as the user 'NoFloatingTrees'");
		}
		
		this.scheduler.scheduleSyncRepeatingTask(this, new LogDecayTask(this), 10, 20 * this.config.getInt(Config.DECAY_FREQUENCY));
		
		this.permissionManager.registerPermissions(Permission.class);
		this.pluginManager.registerEvents(new TreeBreakListener(this), this);
		this.commandManager.registerCommandExecutor(new NftExecutor(this));
		
		if (this.config.getBoolean(Config.ENABLE_PROFILER)){
			this.enableProfiling();
		}
	}
	
	public void onDisable(){
		this.decayQueue.save();
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
	
	/**
	 * Get tree of a block
	 * 
	 * @param block The block to check for a tree
	 * @param force if true, ignore not being directly above dirt/grass, or not under another log, or if topmost block is not leaves 
	 * @return {@link ArrayList<Block>} of tree log blocks, or null
	 */
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
	
}

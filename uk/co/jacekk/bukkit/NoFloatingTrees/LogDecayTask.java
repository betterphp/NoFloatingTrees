package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class LogDecayTask implements Runnable {
	
	public NoFloatingTrees plugin;
	
	public LogDecayTask(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	private boolean isTreeBlock(Material type){
		return (type == Material.LEAVES || type == Material.LOG);
	}
	
	private boolean isTreeBlock(Block block){
		return this.isTreeBlock(block.getType());
	}
	
	private boolean isTreeBranch(Block block){
		ArrayList<Material> faceBlockTypes = new ArrayList<Material>();
		int totalTreeBlocks = 0;
		
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.NORTH).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.NORTH_EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.NORTH_WEST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.SOUTH).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.SOUTH_EAST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.SOUTH_WEST).getType());
		faceBlockTypes.add(block.getRelative(BlockFace.WEST).getType());
		
		for (Material type : faceBlockTypes){
			if (type == Material.LEAVES){
				++totalTreeBlocks;
			}
		}
		
		return (totalTreeBlocks >= 5);
	}
	
	public void run(){
		plugin.log.info("Queue Size: " + plugin.locations.size());
		
		for (Location location : plugin.locations.getAll()){
			if (location.getChunk().isLoaded()){
				
			}
		}
	}

}

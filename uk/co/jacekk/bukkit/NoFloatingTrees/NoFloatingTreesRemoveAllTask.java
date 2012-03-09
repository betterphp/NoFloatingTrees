package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class NoFloatingTreesRemoveAllTask implements Runnable {
	
	public NoFloatingTrees plugin;
	
	public boolean isRunning;
	
	public NoFloatingTreesRemoveAllTask(NoFloatingTrees instance){
		this.plugin = instance;
		
		this.isRunning = false;
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
	
	private boolean areaSafeForRemoval(HashMap<Material, Integer> blockDist){
		int totalBlocks = 0;
		
		if (blockDist.containsKey(Material.GLASS)) return false;
		
		if (blockDist.containsKey(Material.WOOD)) return false;
		
		if (blockDist.containsKey(Material.COBBLESTONE)) return false;
		
		if (blockDist.containsKey(Material.STEP)) return false;
		
		if (blockDist.containsKey(Material.LEAVES) == false) return false;
		
		for (Integer total : blockDist.values()){
			totalBlocks += total;
		}
		
		if ((blockDist.get(Material.LEAVES) / totalBlocks) > 0.10){
			return false;
		}
		
		return true;
	}
	
	public void run(){
		if (this.isRunning) return;
		
		this.isRunning = true;
		
		for (UUID worldId : plugin.coordList.keySet()){
			World world = plugin.getServer().getWorld(worldId);
			
			for (int[] coords : plugin.coordList.get(worldId)){
				int x = coords[0];
				int z = coords[1];
				
				int treeMax = world.getHighestBlockYAt(x, z);
				int treeMin = 0;
				int radius = 0;
				
				HashMap<Material, Integer> blockDist = new HashMap<Material, Integer>();
				ArrayList<Chunk> unloadChunks = new ArrayList<Chunk>();
				
				Chunk chunk = world.getChunkAt(new Location(world, x, 0, z));
				
				if (chunk.isLoaded() == false){
					chunk.load(false);
					unloadChunks.add(chunk);
				}
				
				for (int y = treeMax; y > 0; --y){
					Material type = world.getBlockAt(x, y, z).getType();
					
					if (type == Material.DIRT || type == Material.GRASS){
						treeMin = y;
						
						break;
					}
					
					if (blockDist.containsKey(type)){
						blockDist.put(type, blockDist.get(type) + 1);
					}else{
						blockDist.put(type, 1);
					}
				}
				
				if (blockDist.containsKey(Material.AIR)){
					for (int y = treeMin; y <= treeMax; ++y){
						Block block = world.getBlockAt(x, y, z);
						int branchLength = 1;
						
						do {
							++branchLength;
						} while ((this.isTreeBlock(block.getRelative(BlockFace.NORTH, branchLength)) && this.isTreeBlock(block.getRelative(BlockFace.SOUTH, branchLength))) || (this.isTreeBlock(block.getRelative(BlockFace.EAST, branchLength)) && this.isTreeBlock(block.getRelative(BlockFace.WEST, branchLength))));
						
						if (branchLength > radius){
							radius = branchLength;
						}
					}
				}
				
				if (radius > 8){
					radius = 8;
				}
				
				blockDist.clear();
				
				int startX = x - radius;
				int endX = x + radius;
				int startZ = z - radius;
				int endZ = z + radius;
				
				for (int bx = startX; bx <= endX; ++bx){
					for (int bz = startZ; bz <= endZ; ++bz){
						Chunk chunkHere = world.getChunkAt(new Location(world, bx, 0, bz));
						
						if (chunkHere.isLoaded() == false){
							chunkHere.load(false);
							unloadChunks.add(chunkHere);
						}
						
						for (int y = treeMin; y <= treeMax; ++y){
							Block block = world.getBlockAt(bx, y, bz);
							Material type = block.getType();
							
							if (blockDist.containsKey(type)){
								blockDist.put(type, blockDist.get(type) + 1);
							}else{
								blockDist.put(type, 1);
							}
						}
					}
				}
				
				if (this.areaSafeForRemoval(blockDist)){
					for (int y = treeMin; y <= treeMax; ++y){
						for (int bx = startX; bx <= endX; ++bx){
							for (int bz = startZ; bz <= endZ; ++bz){
								Block block = world.getBlockAt(bx, y, bz);
								
								if (block.getType() == Material.LOG  && (this.isTreeBranch(block) || (bx == x && bz == z))){
									if (plugin.lb != null){
										plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
									}
									
									block.setType(Material.AIR);
								}
							}
						}
					}
				}
				
				for (Chunk unload : unloadChunks){
					unload.unload(true, false);
				}
			}
			
			plugin.coordList.put(worldId, new ArrayList<int[]>());
		}
		
		this.isRunning = false;
	}

}

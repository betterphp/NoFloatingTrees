package uk.co.jacekk.bukkit.NoFloatingTrees.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.block.Block;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.baseplugin.v1.BaseObject;

public class DecayQueue extends BaseObject<NoFloatingTrees> {
	
	private LinkedHashMap<ChunkLocation, LinkedHashMap<Long, BlockLocation>> queue;
	private File storageFile;
	
	public DecayQueue(NoFloatingTrees plugin, File storageFile){
		super(plugin);
		
		this.queue = new LinkedHashMap<ChunkLocation, LinkedHashMap<Long, BlockLocation>>();
		this.storageFile = storageFile;
		
		if (!this.storageFile.exists()){
			// TODO
		}
	}
	
	public void save(){
		// TODO
	}
	
	public void load(){
		// TODO
	}
	
	public int size(){
		int size = 0;
		
		for (LinkedHashMap<Long, BlockLocation> blocks : this.queue.values()){
			size += blocks.size();
		}
		
		return size;
	}
	
	public void clear(){
		this.queue.clear();
	}
	
	public void removeAll(List<BlockLocation> locations){
		for (LinkedHashMap<Long, BlockLocation> blocks : this.queue.values()){
			blocks.values().removeAll(locations);
		}
	}
	
	public void addBlock(Block block){
		long minDecayTime = System.currentTimeMillis() + 40000;
		
		UUID worldUUID = block.getWorld().getUID();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		ChunkLocation chunkLocation = new ChunkLocation(worldUUID, x, z);
		BlockLocation blockLocation = new BlockLocation(worldUUID, x, y, z);
		
		LinkedHashMap<Long, BlockLocation> chunkQueue = this.queue.get(chunkLocation);
		
		if (chunkQueue == null){
			chunkQueue = new LinkedHashMap<Long, BlockLocation>();
			this.queue.put(chunkLocation, chunkQueue);
		}
		
		chunkQueue.put(minDecayTime, blockLocation);
	}
	
	public ArrayList<BlockLocation> getDecayCandidates(){
		ArrayList<BlockLocation> candidates = new ArrayList<BlockLocation>();
		long currentTime = System.currentTimeMillis();
		
		for (Entry<ChunkLocation, LinkedHashMap<Long, BlockLocation>> entry : this.queue.entrySet()){
			if (entry.getKey().getChunk().isLoaded()){
				for (Entry<Long, BlockLocation> blocks : entry.getValue().entrySet()){
					if (currentTime >= blocks.getKey()){
						candidates.add(blocks.getValue());
					}
				}
			}
		}
		
		return candidates;
	}
	
}

package uk.co.jacekk.bukkit.nofloatingtrees.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import uk.co.jacekk.bukkit.baseplugin.v8.BaseObject;
import uk.co.jacekk.bukkit.nofloatingtrees.Config;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;

public class DecayQueue extends BaseObject<NoFloatingTrees> {
	
	private LinkedHashMap<ChunkLocation, LinkedHashMap<Long, BlockLocation>> queue;
	private File storageFile;
	
	public DecayQueue(NoFloatingTrees plugin, File storageFile){
		super(plugin);
		
		this.queue = new LinkedHashMap<ChunkLocation, LinkedHashMap<Long, BlockLocation>>();
		this.storageFile = storageFile;
	}
	
	public void save(){
		try{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(this.storageFile));
			
			output.writeObject(this.queue);
			
			output.flush();
			output.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		if (this.storageFile.exists()){
			try{
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(this.storageFile));
				
				this.queue = (LinkedHashMap<ChunkLocation, LinkedHashMap<Long, BlockLocation>>) input.readObject();
				
				input.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
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
		long minDecayTime = System.currentTimeMillis() + (plugin.config.getInt(Config.DECAY_WAIT_TIME) * 1000);
		
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
			Chunk chunk = entry.getKey().getChunk();
			
			if (chunk != null && chunk.isLoaded()){
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

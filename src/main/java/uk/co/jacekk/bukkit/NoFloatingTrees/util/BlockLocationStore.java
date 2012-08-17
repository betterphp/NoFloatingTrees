package uk.co.jacekk.bukkit.NoFloatingTrees.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class BlockLocationStore {
	
	private HashMap<ChunkLocationStorable, ArrayList<BlockLocationStorable>> locations;
	private File storageFile;
	
	public BlockLocationStore(File storageFile){
		this.locations = new HashMap<ChunkLocationStorable, ArrayList<BlockLocationStorable>>();
		this.storageFile = storageFile;
		
		if (this.storageFile.exists() == false){
			try{
				this.storageFile.createNewFile();
				
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
				
				stream.writeObject(this.locations);
				stream.flush();
				stream.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		try{
			this.locations = (HashMap<ChunkLocationStorable, ArrayList<BlockLocationStorable>>) new ObjectInputStream(new FileInputStream(this.storageFile)).readObject();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(){
		try{
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
			
			stream.writeObject(this.locations);
			stream.flush();
			stream.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean contains(Location location){
		ChunkLocationStorable chunkLocation = new ChunkLocationStorable(location);
		
		for (Entry<ChunkLocationStorable, ArrayList<BlockLocationStorable>> entry : this.locations.entrySet()){
			if (entry.getKey().equals(chunkLocation)){
				for (BlockLocationStorable blockLocation : entry.getValue()){
					if (blockLocation.equals(location)){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void add(Location location){
		if (this.contains(location)){
			return;
		}
		
		ChunkLocationStorable chunkLocation = new ChunkLocationStorable(location);
		BlockLocationStorable blockLocation = new BlockLocationStorable(location);
		
		for (Entry<ChunkLocationStorable, ArrayList<BlockLocationStorable>> entry : this.locations.entrySet()){
			if (entry.getKey().equals(chunkLocation)){
				entry.getValue().add(blockLocation);
				return;
			}
		}
		
		this.locations.put(chunkLocation, new ArrayList<BlockLocationStorable>(Arrays.asList(blockLocation)));
	}
	
	public void remove(Location location){
		ChunkLocationStorable chunkLocation = new ChunkLocationStorable(location);
		BlockLocationStorable blockLocation = new BlockLocationStorable(location);
		
		ArrayList<BlockLocationStorable> blocks;
		
		for (Entry<ChunkLocationStorable, ArrayList<BlockLocationStorable>> entry : this.locations.entrySet()){
			if (entry.getKey().equals(chunkLocation)){
				blocks = entry.getValue();
				
				for (int i = 0; i < blocks.size(); ++i){
					if (blocks.get(i).equals(blockLocation)){
						blocks.remove(i);
						return;
					}
				}
			}
		}
	}
	
	public void removeAll(ArrayList<Location> blocks){
		for (Location location : blocks){
			this.remove(location);
		}
	}
	
	public void clear(){
		this.locations.clear();
	}
	
	public Integer size(boolean deep){
		Integer total = 0;
		
		if (deep){
			for (ArrayList<BlockLocationStorable> blocks : this.locations.values()){
				total += blocks.size();
			}
		}else{
			total = this.locations.size();
		}
		
		return total;
	}
	
	public HashMap<ChunkLocationStorable, ArrayList<BlockLocationStorable>> getAll(){
		return this.locations;
	}
	
	public ArrayList<BlockLocationStorable> getAll(Chunk chunk){
		ChunkLocationStorable chunkLocation = new ChunkLocationStorable(chunk);
		
		for (Entry<ChunkLocationStorable, ArrayList<BlockLocationStorable>> entry : this.locations.entrySet()){
			if (entry.getKey().equals(chunkLocation)){
				return entry.getValue();
			}
		}
		
		return new ArrayList<BlockLocationStorable>();
	}
	
}

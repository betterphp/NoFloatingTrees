package uk.co.jacekk.bukkit.nofloatingtrees.storage;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkLocation implements Serializable {
	
	private static final long serialVersionUID = -5587076524001385361L;
	
	private UUID worldUUID;
	private int x, z;
	
	public ChunkLocation(UUID worldUUID, int x, int z){
		this.worldUUID = worldUUID;
		
		this.x = x;
		this.z = z;
	}
	
	public ChunkLocation(World world, int x, int z){
		this(world.getUID(), x, z);
	}
	
	public Chunk getChunk(boolean ignoreUnloaded){
		World world = Bukkit.getWorld(this.worldUUID);
		
		if (world == null){
			return null;
		}
		
		if (ignoreUnloaded && !world.isChunkLoaded(this.x, this.z)){
			return null;
		}
		
		return world.getChunkAt(this.x, this.z);
	}
	
}

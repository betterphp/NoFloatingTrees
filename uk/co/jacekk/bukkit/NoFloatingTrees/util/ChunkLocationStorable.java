package uk.co.jacekk.bukkit.NoFloatingTrees.util;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkLocationStorable implements Serializable {
	
	private static final long serialVersionUID = -4300562242289782992L;
	
	private UUID worldUUID;
	private Integer x;
	private Integer z;
	
	public ChunkLocationStorable(Chunk chunk){
		this.worldUUID = chunk.getWorld().getUID();
		
		this.x = chunk.getX();
		this.z = chunk.getZ();
	}
	
	public ChunkLocationStorable(Location location){
		this(location.getChunk());
	}
	
	public boolean equals(ChunkLocationStorable location){
		if (!location.getX().equals(this.x)) return false;
		if (!location.getZ().equals(this.z)) return false;
		
		if (!location.getWorldUUID().equals(this.worldUUID)) return false;
		
		return true;
	}
	
	public World getWorld(){
		return Bukkit.getWorld(this.worldUUID);
	}
	
	public UUID getWorldUUID(){
		return this.worldUUID;
	}
	
	public Integer getX(){
		return this.x;
	}
	
	public Integer getZ(){
		return this.z;
	}
	
	public Chunk getChunk(){
		return this.getWorld().getChunkAt(this.x, this.z);
	}
	
}

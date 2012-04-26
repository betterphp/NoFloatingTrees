package uk.co.jacekk.bukkit.NoFloatingTrees.util;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocationStorable implements Serializable {
	
	private static final long serialVersionUID = 4050384430959509986L;
	
	private UUID worldUUID;
	private Integer x;
	private Integer y;
	private Integer z;
	
	public BlockLocationStorable(Block block){
		this.worldUUID = block.getWorld().getUID();
		
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}
	
	public BlockLocationStorable(Location location){
		this(location.getBlock());
	}
	
	public boolean equals(Location location){
		if (location.getBlockX() != this.x) return false;
		if (location.getBlockY() != this.y) return false;
		if (location.getBlockZ() != this.z) return false;
		
		if (location.getWorld().getUID() != this.worldUUID) return false;
		
		return true;
	}
	
	public boolean equals(BlockLocationStorable location){
		if (location.getX() != this.x) return false;
		if (location.getY() != this.y) return false;
		if (location.getZ() != this.z) return false;
		
		if (location.getWorldUUID() != this.worldUUID) return false;
		
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
	
	public Integer getY(){
		return this.y;
	}
	
	public Integer getZ(){
		return this.z;
	}
	
	public Block getBlock(){
		return this.getWorld().getBlockAt(this.x, this.y, this.z);
	}
	
	public Location getLocation(){
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}
	
}

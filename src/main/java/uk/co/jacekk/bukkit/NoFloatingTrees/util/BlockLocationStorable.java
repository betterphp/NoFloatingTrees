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
	
	public boolean equals(BlockLocationStorable location){
		if (!location.getX().equals(this.x)) return false;
		if (!location.getY().equals(this.y)) return false;
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
	
	public Integer getY(){
		return this.y;
	}
	
	public Integer getZ(){
		return this.z;
	}
	
	public Block getBlock(){
		World world = this.getWorld();
		
		if (world != null){
			return this.getWorld().getBlockAt(this.x, this.y, this.z);
		}
		
		return null;
	}
	
	public Location getLocation(){
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}
	
}

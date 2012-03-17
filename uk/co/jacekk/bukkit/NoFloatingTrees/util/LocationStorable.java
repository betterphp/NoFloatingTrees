package uk.co.jacekk.bukkit.NoFloatingTrees.util;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationStorable implements Serializable {
	
	private static final long serialVersionUID = 4050384430959509986L;
	
	private UUID worldUUID;
	private Integer x;
	private Integer y;
	private Integer z;
	
	public LocationStorable(Location location){
		this.worldUUID = location.getWorld().getUID();
		
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
	}
	
	public World getWorld(){
		return Bukkit.getWorld(this.worldUUID);
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
	
	public Location getLocation(){
		return new Location(this.getWorld(), this.getX(), this.getY(), this.getZ());
	}
	
}

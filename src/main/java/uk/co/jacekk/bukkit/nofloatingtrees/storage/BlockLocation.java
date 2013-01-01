package uk.co.jacekk.bukkit.nofloatingtrees.storage;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocation implements Serializable {
	
	private static final long serialVersionUID = -2466733551818655266L;
	
	private UUID worldUUID;
	private int x, y, z;
	
	public BlockLocation(UUID worldUUID, int x, int y, int z){
		this.worldUUID = worldUUID;
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockLocation(World world, int x, int y, int z){
		this(world.getUID(), x, y, z);
	}
	
	public Block getBlock(){
		World world = Bukkit.getWorld(this.worldUUID);
		
		if (world == null){
			return null;
		}
		
		return world.getBlockAt(this.x, this.y, this.z);
	}
	
}

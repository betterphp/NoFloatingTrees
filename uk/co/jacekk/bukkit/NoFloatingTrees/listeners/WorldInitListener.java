package uk.co.jacekk.bukkit.NoFloatingTrees.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;

public class WorldInitListener implements Listener {

	NoFloatingTrees plugin;
	
	public WorldInitListener(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldInit(WorldInitEvent event){
		UUID uuid = event.getWorld().getUID();
		
		if (plugin.coordList.containsKey(uuid) == false){
			plugin.coordList.put(uuid, new ArrayList<int[]>());
		}
	}
	
}

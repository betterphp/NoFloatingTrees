package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

import uk.co.jacekk.bukkit.NoFloatingTrees.storage.BlockLocation;
import uk.co.jacekk.bukkit.baseplugin.v7.scheduler.BaseTask;

public class LogDecayTask extends BaseTask<NoFloatingTrees> {
	
	private Random rand;
	
	public LogDecayTask(NoFloatingTrees plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	public void run(){
		ArrayList<BlockLocation> remove = new ArrayList<BlockLocation>();
		
		for (BlockLocation blockLocation : plugin.decayQueue.getDecayCandidates()){
			Block block = blockLocation.getBlock();
			Material type = block.getType();
			
			if (type != Material.LOG){
				remove.add(blockLocation);
			}else if (this.rand.nextInt(100) < plugin.config.getInt(Config.DECAY_CHANCE)){
				if (rand.nextInt(100) < plugin.config.getInt(Config.DECAY_DROP_CHANCE)){
					block.breakNaturally();
				}else{
					block.setType(Material.AIR);
				}
				
				if (plugin.lb != null){
					plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
				}
				
				remove.add(blockLocation);
			}
		}
		
		plugin.decayQueue.removeAll(remove);
	}

}

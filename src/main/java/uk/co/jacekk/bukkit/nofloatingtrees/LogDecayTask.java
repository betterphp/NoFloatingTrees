package uk.co.jacekk.bukkit.nofloatingtrees;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;

import uk.co.jacekk.bukkit.baseplugin.scheduler.BaseTask;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.BlockLocation;

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
                if (plugin.logblock != null){
            		plugin.logblock.queueBlockBreak("NoFloatingTrees", block.getState());
            	}

				if (rand.nextInt(100) < plugin.config.getInt(Config.DECAY_DROP_CHANCE)){
					block.breakNaturally();
				}else{
					block.setType(Material.AIR);
				}
				
				remove.add(blockLocation);
			}
		}
		
		plugin.decayQueue.removeAll(remove);
	}

}

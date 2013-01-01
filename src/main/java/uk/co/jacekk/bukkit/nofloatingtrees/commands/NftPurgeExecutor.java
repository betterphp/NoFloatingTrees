package uk.co.jacekk.bukkit.nofloatingtrees.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v7.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v7.command.CommandHandler;
import uk.co.jacekk.bukkit.nofloatingtrees.Config;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.BlockLocation;

public class NftPurgeExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	private Random rand;
	
	public NftPurgeExecutor(NoFloatingTrees plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	@CommandHandler(names = {"nftpurge"}, description = "Removes ALL the logs.", usage = "[true/false]")
	public void nftpurge(CommandSender sender, String label, String[] args){
		if (!sender.hasPermission("nofloatingtrees.command.purge")){
			sender.sendMessage(ChatColor.RED + "you do not have permission to use this command");
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /nftpurge <drop>");
			sender.sendMessage(ChatColor.RED + "Example: /nftpurge true");
			return;
		}
		
		boolean drop = args[0].equalsIgnoreCase("true");
		Integer size = plugin.decayQueue.size();
		
		for (BlockLocation blockLocation : plugin.decayQueue.getDecayCandidates()){
			Block block = blockLocation.getBlock();
			Material type = block.getType();
			
			if (type == Material.LOG){
				if (drop && rand.nextInt(100) < plugin.config.getInt(Config.DECAY_DROP_CHANCE)){
					block.breakNaturally();
				}else{
					block.setType(Material.AIR);
				}
				
				if (plugin.lb != null){
					plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
				}
			}
		}
		
		plugin.decayQueue.clear();
		
		sender.sendMessage(ChatColor.GREEN + "Removed " + size + " blocks.");
	}
	
}

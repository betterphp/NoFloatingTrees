package uk.co.jacekk.bukkit.nofloatingtrees.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v9.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v9.command.SubCommandHandler;
import uk.co.jacekk.bukkit.nofloatingtrees.Config;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.BlockLocation;

public class NftExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	public NftExecutor(NoFloatingTrees plugin){
		super(plugin);
	}
	
	public void nft(CommandSender sender, String label, String[] args){
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <option> <args>");
		sender.sendMessage(ChatColor.RED + "Options:");
		sender.sendMessage(ChatColor.RED + "  queue - Shows the size of the decay queue");
		sender.sendMessage(ChatColor.RED + "  purge - Forces all waiting blocks to decay");
	}
	
	@SubCommandHandler(parent = "nft", name = "queue")
	public void nftQueue(CommandSender sender, String label, String[] args){
		if (!sender.hasPermission("nofloatingtrees.command.debug")){
			sender.sendMessage(ChatColor.RED + "you do not have permission to use this command");
			return;
		}
		
		int size = plugin.decayQueue.size();
		
		sender.sendMessage(ChatColor.BLUE + "Decay Queue Size " + ((size > 1000) ? ChatColor.RED : ChatColor.GREEN) + size);
	}
	
	@SubCommandHandler(parent = "nft", name = "purge")
	public void nftPurge(CommandSender sender, String label, String[] args){
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
		Random rand = new Random();
		
		for (BlockLocation blockLocation : plugin.decayQueue.getDecayCandidates()){
			Block block = blockLocation.getBlock();
			Material type = block.getType();
			
			if (type == Material.LOG){
				if (drop && rand.nextInt(100) < plugin.config.getInt(Config.DECAY_DROP_CHANCE)){
					block.breakNaturally();
				}else{
					block.setType(Material.AIR);
				}
				
				if (plugin.logblock != null){
					plugin.logblock.queueBlockBreak("NoFloatingTrees", block.getState());
				}
			}
		}
		
		plugin.decayQueue.clear();
		
		sender.sendMessage(ChatColor.GREEN + "Removed " + size + " blocks.");
	}
	
}

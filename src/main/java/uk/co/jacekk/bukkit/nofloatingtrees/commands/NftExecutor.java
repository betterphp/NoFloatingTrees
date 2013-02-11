package uk.co.jacekk.bukkit.nofloatingtrees.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.baseplugin.v9.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandHandler;
import uk.co.jacekk.bukkit.baseplugin.v9.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.v9.command.SubCommandHandler;
import uk.co.jacekk.bukkit.nofloatingtrees.Config;
import uk.co.jacekk.bukkit.nofloatingtrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.nofloatingtrees.Permission;
import uk.co.jacekk.bukkit.nofloatingtrees.storage.BlockLocation;

public class NftExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	public NftExecutor(NoFloatingTrees plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"nft"}, description = "Used to manage the decay queue", usage = "<option>")
	@CommandTabCompletion({"queue|purge"})
	public void nft(CommandSender sender, String label, String[] args){
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <option> <args>");
		sender.sendMessage(ChatColor.RED + "Options:");
		
		if (Permission.QUEUE_SIZE.has(sender)){
			sender.sendMessage(ChatColor.RED + "  queue - Shows the size of the decay queue");
		}
		
		if (Permission.QUEUE_PURGE.has(sender)){
			sender.sendMessage(ChatColor.RED + "  purge - Forces all waiting blocks to decay");
		}
	}
	
	@SubCommandHandler(parent = "nft", name = "queue")
	public void nftQueue(CommandSender sender, String label, String[] args){
		if (!Permission.QUEUE_SIZE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		int size = plugin.decayQueue.size();
		
		sender.sendMessage(ChatColor.BLUE + "Decay Queue Size " + ((size > 1000) ? ChatColor.RED : ChatColor.GREEN) + size);
	}
	
	@SubCommandHandler(parent = "nft", name = "purge")
	@CommandTabCompletion({"true|false"})
	public void nftPurge(CommandSender sender, String label, String[] args){
		if (!Permission.QUEUE_PURGE.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /" + label + " purge <drop>");
			sender.sendMessage(ChatColor.RED + "Example: /" + label + " purge true");
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

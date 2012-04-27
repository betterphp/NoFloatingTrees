package uk.co.jacekk.bukkit.NoFloatingTrees.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;

public class NftDebugExecutor implements CommandExecutor {
	
private NoFloatingTrees plugin;
	
	public NftDebugExecutor(NoFloatingTrees plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!sender.hasPermission("nofloatingtrees.command.debug")){
			sender.sendMessage(ChatColor.RED + "you do not have permission to use this command");
			return true;
		}
		
		Integer size = plugin.blockLocations.size(true);
		
		sender.sendMessage(ChatColor.BLUE + "Decay Queue Size " + ((size > 1000) ? ChatColor.RED : ChatColor.GREEN) + size);
		
		return true;
	}
	
}

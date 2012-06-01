package uk.co.jacekk.bukkit.NoFloatingTrees.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class NftDebugExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	public NftDebugExecutor(NoFloatingTrees plugin){
		super(plugin);
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

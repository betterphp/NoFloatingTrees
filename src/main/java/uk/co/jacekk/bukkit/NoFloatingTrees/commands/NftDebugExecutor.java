package uk.co.jacekk.bukkit.NoFloatingTrees.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.baseplugin.v1.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.v1.command.CommandHandler;

public class NftDebugExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	public NftDebugExecutor(NoFloatingTrees plugin){
		super(plugin);
	}
	
	@CommandHandler(names = {"nftdebug"}, description = "Gets some debug information")
	public void onCommand(CommandSender sender, String label, String[] args){
		if (!sender.hasPermission("nofloatingtrees.command.debug")){
			sender.sendMessage(ChatColor.RED + "you do not have permission to use this command");
			return;
		}
		
		int size = plugin.decayQueue.size();
		
		sender.sendMessage(ChatColor.BLUE + "Decay Queue Size " + ((size > 1000) ? ChatColor.RED : ChatColor.GREEN) + size);
	}
	
}

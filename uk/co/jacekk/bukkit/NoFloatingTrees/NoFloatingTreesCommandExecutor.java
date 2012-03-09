package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoFloatingTreesCommandExecutor implements CommandExecutor {
	
	private NoFloatingTrees plugin;
	
	public NoFloatingTreesCommandExecutor(NoFloatingTrees instance){
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (sender instanceof Player && sender.hasPermission("nofloatingtrees.command.nftpurge") == false){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command !");
		}else{
			int total = 0;
			
			for (ArrayList<int[]> coordList : plugin.coordList.values()){
				total += coordList.size();
			}
			
			sender.sendMessage(ChatColor.GREEN + "Removing " + total + " floating " + ((total > 1) ? "trees" : "tree") + ".");
			
			plugin.removeAllTask.run();
			
			sender.sendMessage(ChatColor.GREEN + "Done !");
		}
		
		return true;
	}
	
}

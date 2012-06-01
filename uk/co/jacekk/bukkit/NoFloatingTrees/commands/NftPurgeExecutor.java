package uk.co.jacekk.bukkit.NoFloatingTrees.commands;

import java.util.ArrayList;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.NoFloatingTrees.NoFloatingTrees;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.BlockLocationStorable;
import uk.co.jacekk.bukkit.NoFloatingTrees.util.ChunkLocationStorable;
import uk.co.jacekk.bukkit.baseplugin.BaseCommandExecutor;

public class NftPurgeExecutor extends BaseCommandExecutor<NoFloatingTrees> {
	
	private Random rand;
	
	public NftPurgeExecutor(NoFloatingTrees plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if (!sender.hasPermission("nofloatingtrees.command.purge")){
			sender.sendMessage(ChatColor.RED + "you do not have permission to use this command");
			return true;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Usage: /nftpurge <drop>");
			sender.sendMessage(ChatColor.RED + "Example: /nftpurge true");
			return true;
		}
		
		boolean drop = args[0].equalsIgnoreCase("true");
		Integer size = plugin.blockLocations.size(true);
		
		Block block;
		Material type;
		Location location;
		
		for (Entry<ChunkLocationStorable, ArrayList<BlockLocationStorable>> chunks : plugin.blockLocations.getAll().entrySet()){
			for (BlockLocationStorable blockLocation : chunks.getValue()){
				block = blockLocation.getBlock();
				type = block.getType();
				location = block.getLocation();
				
				if (type == Material.LOG){
					if (drop && this.rand.nextInt(100) < 15){
						location.getWorld().dropItemNaturally(location, new ItemStack(type, 1, (short) 0, block.getData()));
					}
					
					block.setType(Material.AIR);
					
					if (plugin.lb != null){
						plugin.lb.queueBlockBreak("NoFloatingTrees", block.getState());
					}
				}
			}
		}
		
		plugin.blockLocations.clear();
		
		sender.sendMessage(ChatColor.GREEN + "Removed " + size + " blocks.");
		
		return true;
	}
	
}

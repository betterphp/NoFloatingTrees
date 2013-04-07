package uk.co.jacekk.bukkit.nofloatingtrees;

import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.baseplugin.permissions.PluginPermission;

public class Permission {
	
	public static final PluginPermission QUEUE_SIZE		= new PluginPermission("nofloatingtrees.queue.size",	PermissionDefault.OP,	"Allows the player to view the size of the decay queue");
	public static final PluginPermission QUEUE_PURGE	= new PluginPermission("nofloatingtrees.queue.purge",	PermissionDefault.OP,	"Allows the player to purge the decay queue");
	
}

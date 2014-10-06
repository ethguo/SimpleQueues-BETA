package com.github.ethg242.simplequeues.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;
import com.github.ethg242.simplequeues.misc.MessageColor;


public class CmdRemoveExecutor implements CommandExecutor {
	private SimpleQueues plugin;	 
	
	public CmdRemoveExecutor(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			
			// Get the queue and check if it exists
			Queue queue = plugin.getQueue(args[0]);
			if (queue == null) {
				sender.sendMessage(MessageColor.PROBLEM + "Could not find a queue with the name " + MessageColor.HIGHLIGHT + args[0]);
				return true;
			}
			
			// ./qremove <queue>
			if (args.length == 1 && sender instanceof Player && sender.hasPermission("SimpleQueues.remove.self")) {
				Player player = (Player) sender;
				// Remove sender from queue
				boolean success = queue.remove(player);
				if (success) {
					player.sendMessage(MessageColor.NORMAL + "You have been removed from the queue " + MessageColor.HIGHLIGHT + queue.name);
					plugin.getServer().getLogger().fine(player.getName() + " has been removed from the queue " + queue.name);
				}
				else {
					player.sendMessage(MessageColor.PROBLEM + "You are not in the queue " + MessageColor.HIGHLIGHT + queue.name);
				}
				return true;
			}
			
			// ./qremove <queue> [player]
			else if (args.length == 2 && sender.hasPermission("SimpleQueues.remove.other")) {
				// Get the player and check if it exists
				Player player = plugin.getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(MessageColor.HIGHLIGHT + args[1] + MessageColor.PROBLEM + " is not a valid player or is not online.");
					return true;
				}

				// Remove the player
				boolean success = queue.remove(player);
				if (success) {
					sender.sendMessage(MessageColor.HIGHLIGHT + player.getName() + MessageColor.NORMAL + " has been removed from the queue " + MessageColor.HIGHLIGHT + queue.name);
					player.sendMessage(MessageColor.NORMAL + "You have been removed from the queue " + MessageColor.HIGHLIGHT + queue.name + MessageColor.NORMAL + " by " + MessageColor.HIGHLIGHT + sender.getName());
					plugin.getServer().getLogger().fine(player.getName() + " has been removed from the queue " + queue.name + " by " + sender.getName());
				}
				else {
					sender.sendMessage(MessageColor.HIGHLIGHT + player.getName() + MessageColor.PROBLEM + " is not in the queue " + MessageColor.HIGHLIGHT + queue.name);
				}
				return true;
			}
		}
		return false;
	}
}

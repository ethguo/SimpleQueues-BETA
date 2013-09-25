package com.github.ethg242.simplequeues.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;


public class CmdAddExecutor implements CommandExecutor {
	private SimpleQueues plugin;
	
	public CmdAddExecutor(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			
			// Get the queue and check if it exists
			Queue queue = plugin.getQueue(args[0]);
			if (queue == null) {
				sender.sendMessage("Could not find a queue with the name " + args[0]);
				return true;
			}
			
			// ./qadd <queue>
			if (args.length == 1 && sender instanceof Player && sender.hasPermission("SimpleQueues.add.self")) {
				// Add sender to queue
				int result = queue.add((Player) sender);
				if (result == 2) {
					sender.sendMessage("The queue " + queue.name + " is already full.");
				}
				else if (result == 1) {
					sender.sendMessage("You are already in the queue " + queue.name);
				} else {
					sender.sendMessage("You have been added to the queue " + queue.name);
				}
				return true;
			}
			
			// ./qadd <queue> [player]
			else if (args.length == 2 && sender.hasPermission("SimpleQueues.add.other")) {
				// Get the player and check if it exists
				Player player = plugin.getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(args[1] + " is not a valid player or is not online.");
					return true;
				}
				
				// Add the player
				int result = queue.add(player);
				if (result == 2) {
					sender.sendMessage("The queue " + queue.name + " is already full.");
				}
				else if (result == 1) {
					sender.sendMessage(player.getName() + " is already in the queue " + queue.name);
				}
				else {
					sender.sendMessage(player.getName() + " has been added to the queue " + queue.name);
					player.sendMessage("You have been added to the queue " + queue.name + " by " + sender.getName());
					plugin.getServer().getLogger().fine(player.getName() + " has been added to the queue " + queue.name + " by " + sender.getName());
				}
				return true;
			}
		}
		return false;
	}
}
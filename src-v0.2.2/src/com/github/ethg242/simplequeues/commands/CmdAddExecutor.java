package com.github.ethg242.simplequeues.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;
import com.github.ethg242.simplequeues.misc.MessageColor;


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
				sender.sendMessage(MessageColor.PROBLEM + "Could not find a queue with the name " + MessageColor.HIGHLIGHT + args[0]);
				return true;
			}
			
			// ./qadd <queue>
			if (args.length == 1 && sender instanceof Player && sender.hasPermission("SimpleQueues.add.self")) {
				// Add sender to queue
				queue.add((Player) sender);
				return true;
			}
			
			// ./qadd <queue> [player]
			else if (args.length == 2 && sender.hasPermission("SimpleQueues.add.other")) {
				// Get the player and check if it exists
				Player player = plugin.getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage(MessageColor.HIGHLIGHT + args[1] + MessageColor.PROBLEM + " is not a valid player or is not online.");
					return true;
				}
				
				// Add the player
				queue.add(player, (Player) sender);
				return true;
			}
		}
		return false;
	}
}
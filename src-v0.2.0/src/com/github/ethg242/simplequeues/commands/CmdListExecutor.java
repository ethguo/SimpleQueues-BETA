package com.github.ethg242.simplequeues.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;


public class CmdListExecutor implements CommandExecutor {
	private SimpleQueues plugin;
	
	public CmdListExecutor(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1 && sender.hasPermission("SimpleQueues.list")) {
			
			// ./qlist -a
			if (args[0].equalsIgnoreCase("-a")) {
				// Iterate and write name of all queues
				sender.sendMessage("All queues:");
				for (Queue queue : plugin.queues) {
					if (queue != null) {
						sender.sendMessage(queue.name);
					}
				}
			}
			
			// ./qlist <queue>
			else {
				// Get the queue and check if it exists
				Queue queue = plugin.getQueue(args[0]);
				if (queue == null) {
					sender.sendMessage("Could not find a queue with the name " + args[0]);
					return true;
				}
				
				// Write queue
				sender.sendMessage("Current queue:\n" + queue.toString());
			}
			return true;
		}
		return false;
	}
}

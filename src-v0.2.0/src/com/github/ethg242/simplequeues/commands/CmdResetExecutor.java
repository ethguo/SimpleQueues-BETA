package com.github.ethg242.simplequeues.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;


public class CmdResetExecutor implements CommandExecutor {
	private SimpleQueues plugin;
	
	public CmdResetExecutor(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1 && sender.hasPermission("SimpleQueues.reset")) {
			
			// ./qreset -a
			if (args[0].equalsIgnoreCase("-a")) {
				// Iterate and reset all queues
				for (Queue queue : plugin.queues) {
					if (queue != null) {
						queue.reset();
					}
				}
				sender.sendMessage("All queues have been reset.");
				plugin.getServer().getLogger().fine("All queues have been reset by " + sender.getName());
			}
			
			// ./qreset <queue>
			else {
				// Get the queue and check if it exists
				Queue queue = plugin.getQueue(args[0]);
				if (queue == null) {
					sender.sendMessage("Could not find a queue with the name " + args[0]);
					return true;
				}
				
				// Reset queue
				queue.reset();
				sender.sendMessage("The queue " + queue.name + " has been reset.");
				plugin.getServer().getLogger().fine("The queue " + queue.name + " has been reset by " + sender.getName());
				return true;
			}
		}
		return false;
	}
}

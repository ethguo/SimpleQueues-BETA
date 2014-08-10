package com.github.ethg242.simplequeues.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.Queue;
import com.github.ethg242.simplequeues.SimpleQueues;
import com.github.ethg242.simplequeues.misc.Helpers;
import com.github.ethg242.simplequeues.misc.LocationFormatException;
import com.github.ethg242.simplequeues.misc.MessageColor;

public class CmdConfigExecutor implements CommandExecutor {
	private SimpleQueues plugin;
	
	public CmdConfigExecutor(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Creates a queue
		if (args.length >= 3 && args[0].equalsIgnoreCase("create")) {
			
			// Get the size of the queue
			int queueSize;
			try {
				queueSize = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException e) {
				sender.sendMessage(MessageColor.HIGHLIGHT + args[2] + MessageColor.PROBLEM + " is not a valid number.");
				return true;
			}
			
			// Get the tpLocation for the queue
			Location queueTpLocation = null;
			// ./qconfig create <name> <size> [tplocation]
			if (args.length == 7) {
				// Try to get the location
				try {
					queueTpLocation = Helpers.parseLocation(plugin, args[3], args[4], args[5], args[6]);
				}
				catch (LocationFormatException e) {
					sender.sendMessage(e.getMessage());
					return true;
				}
			}
			// ./qconfig create <name> <size> and sender is a player
			else if (args.length == 3 && sender instanceof Player) {
				// Use its location
				queueTpLocation = ((Player) sender).getLocation();
			}
			else {
				sender.sendMessage(MessageColor.PROBLEM + "You must either provide coordinates or be a player.");
			}
			
			// problems, notify sender
			boolean success = plugin.createQueue(args[1], queueSize, queueTpLocation);
			
			if (!success) {
				sender.sendMessage(MessageColor.PROBLEM + "There is already a queue with that name, or there are already " + plugin.queues.length + " queues on the server.");
				return true;
			}
			
			sender.sendMessage(MessageColor.NORMAL + "Successfully created queue " + 
						MessageColor.HIGHLIGHT + args[1] + MessageColor.NORMAL + 
						" with size " + MessageColor.HIGHLIGHT + queueSize + MessageColor.NORMAL + 
						" and tplocation " + MessageColor.HIGHLIGHT + queueTpLocation.toString());
			
			plugin.getLogger().info(sender.getName() + " created queue " + args[1] + " with size " + queueSize + " and tplocation " + queueTpLocation.toString());
			return true;
		}
		
		/* // Edits a queue
		else if (args.length == 4 && args[0].equalsIgnoreCase("edit")) {
			
			// Get the queue and check if it exists
			Queue queue = plugin.getQueue(args[1]);
			
			if (queue == null) {
				sender.sendMessage("Could not find a queue with the name " + args[1]);
				return true;
			}
			
			// Get the value
			String value = args[3];
			
			// ./qconfig edit <queue> name <value>
			if (args[2].equalsIgnoreCase("name")) {
				queue.name = value;
				sender.sendMessage("Successfully renamed queue " + args[1] + " to " + queue.name);
				plugin.getLogger().info("Renamed queue " + args[1] + " to " + queue.name);
			}
			
			// ./qconfig edit <queue> size <value>
			if (args[2].equalsIgnoreCase("size")) {
				queue.size = Integer.parseInt(value);
				sender.sendMessage("Successfully resized queue " + queue.name + " to " + value);
				plugin.getLogger().info("Resized queue " + queue.name + " to " + value);
			}
			
			// ./qconfig edit <queue> tplocation <value>
			if (args[2].equalsIgnoreCase("tplocation")) {
				try {
					queue.tpLocation = Helpers.parseLocation(plugin, args[3], args[4], args[5], args[6]);
				}
				catch (LocationFormatException e) {
					sender.sendMessage(e.getMessage());
					return true;
				}
				sender.sendMessage("Successfully changed tplocation of queue " + args[1] + " to " + queue.tpLocation.toString());
				plugin.getLogger().info("Changed tplocation of queue " + args[1] + " to " + queue.tpLocation.toString());
			}
			return true;
		} */
		
		// Deletes a queue
		else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
			// Get the queue and check if it exists
			Queue queue = plugin.getQueue(args[1]);
			
			if (queue == null) {
				sender.sendMessage(MessageColor.PROBLEM + "Could not find a queue with the name " + MessageColor.HIGHLIGHT + args[1]);
			}
			// Find the queue by location and delete it
			boolean success = false;
			for (int pos = 0; pos < plugin.queues.length; pos++) {
				if (plugin.queues[pos] != null && plugin.queues[pos].name.equals(queue.name)) {
					plugin.queues[pos] = null;
					success = true;
					break;
				}
			}
			if (success) {
				sender.sendMessage(MessageColor.NORMAL + "Successfully deleted queue " + MessageColor.HIGHLIGHT + queue.name);
				plugin.getLogger().info(sender.getName() + " deleted queue " + args[1]);
			} else {
				sender.sendMessage(MessageColor.PROBLEM + "Was not able to delete queue " + MessageColor.HIGHLIGHT + queue.name);
			}
			return true;
		}
		return false;
	}
}

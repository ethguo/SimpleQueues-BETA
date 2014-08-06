package com.github.ethg242.simplequeues;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ethg242.simplequeues.commands.*;
import com.github.ethg242.simplequeues.misc.Helpers;

public final class SimpleQueues extends JavaPlugin {
	public Queue[] queues;
	
	public Queue getQueue(String queueName) {
		for (Queue queue : queues) {
			if (queue != null) {
				if (queue.name.equals(queueName)) {
					return queue;
				}
			}
		}
		return null;
	}
	
	public boolean createQueue(String name, int size, Location tpLocation) {
		// If the queue name already exists, return false
		try {
			for (Queue q : queues) {
				if (name.equals(q.name)) {
					return false;
				}
			}
		}
		// Stop if we start hitting nulls
		catch (NullPointerException e) { }
		
		// If the queue limit is reached, return false
		int nextEmpty = Helpers.nextEmpty(queues);
		if (nextEmpty == -1) {
			return false;
		}
		
		queues[Helpers.nextEmpty(queues)] = new Queue(this, name, size, tpLocation);
		return true;
	}
	
/*	private String tostring(String[] array) {
		String out = "ARRAY{";
		for (String i : array) {
			out += i + ", ";
		}
		out += "}";
		return out;
	} */

	public void onEnable() {
		// Save the default config file if the current one is unreadable
		saveDefaultConfig();
		
		// Get maxQueues
		int maxQueues = getConfig().getInt("maxQueues");
		queues = new Queue[maxQueues];
/*		try {
			// Read saved queues
			String[] keys;
			keys = (String[]) getConfig().getKeys(true).toArray();
			/*DEBUG/getLogger().finest("[DEBUG] GOT KEYS: " + keys.toString());
			for (String name : keys) {
				if (name.matches("queue.")) {
					// Get fields from config
					int size = getConfig().getInt("queues." + name + ".size");
					int tpX = getConfig().getInt("queues." + name + ".x");
					int tpY = getConfig().getInt("queues." + name + ".y");
					int tpZ = getConfig().getInt("queues." + name + ".z");
					String w = getConfig().getString("queues." + name + ".world");
					/*DEBUG/getLogger().finest("[DEBUG] FOR QUEUE " + name + " GOT VALUES " + tpX + tpY + tpZ + w);
					
					// Get the world
					World tpW = getServer().getWorld(w);
					if (tpW == null) {
						throw new RuntimeException();
					}
					else {
						// Assemble the Location object
						Location tpLocation = new Location(tpW, tpX, tpY, tpZ);
						
						// Calculate the next empty slot in the queue
						int nextEmpty = Helpers.nextEmpty(queues);
						if (nextEmpty != -1) {
							// Create the queue
							queues[nextEmpty] = new Queue(this, name, size, tpLocation);
						}
					}
				}
			}
			getLogger().info("Successfully loaded queues.");
		} */
		
		try {
			// Read saved queues
			Set<String> keys = getConfig().getConfigurationSection("queues").getKeys(false);
			
			for (String name : keys) {
				int size = getConfig().getInt("queues." + name + ".size");
				int tpX = getConfig().getInt("queues." + name + ".x");
				int tpY = getConfig().getInt("queues." + name + ".y");
				int tpZ = getConfig().getInt("queues." + name + ".z");
				String w = getConfig().getString("queues." + name + ".world");
				
				World tpW = getServer().getWorld(w);
				if (tpW == null) {
					throw new RuntimeException();
				}
				
				// Assemble the Location object
				Location tpLocation = new Location(tpW, tpX, tpY, tpZ);
				
				// Calculate the next empty slot in the queue
				int nextEmpty = Helpers.nextEmpty(queues);
				if (nextEmpty != -1) {
					// Create the queue
					queues[nextEmpty] = new Queue(this, name, size, tpLocation);
				}
			}
			getLogger().info("Successfully loaded queues.");
		}
		catch (Exception e) {
			getLogger().warning("The config file (config.yml) for SimpleQueues was corrupted. Unable to load queues.");
		}
		
		// Register events and commands
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getCommand("qlist").setExecutor(new CmdListExecutor(this));
		getCommand("qadd").setExecutor(new CmdAddExecutor(this));
		getCommand("qremove").setExecutor(new CmdRemoveExecutor(this));
		getCommand("qreset").setExecutor(new CmdResetExecutor(this));
		getCommand("qconfig").setExecutor(new CmdConfigExecutor(this));
	}
	

	public void onDisable() {
		getConfig().set("queues", null);
		for (Queue q : queues) {
			if (q != null) {
				// Add queue to config file
				getConfig().set("queues." + q.name + ".size", q.size);
				getConfig().set("queues." + q.name + ".x", (int) q.tpLocation.getX());
				getConfig().set("queues." + q.name + ".y", (int) q.tpLocation.getY());
				getConfig().set("queues." + q.name + ".z", (int) q.tpLocation.getZ());
				getConfig().set("queues." + q.name + ".world", q.tpLocation.getWorld().getName());
			}
		}
		saveConfig();
		getLogger().info("Successfully saved queues.");
	}
}

//TODO: tidy up logging etc.
//TODO: migrate playermessages and logging from CmdRemoveExcecutor to Queue.remove


/* BUGS:
 * [none]
 */
package com.github.ethg242.simplequeues;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.misc.Helpers;

public class Queue {
	private SimpleQueues plugin;
	public String name;
	public int size;
	public Location tpLocation;
	public Player[] queueArray;
	
	public Queue(SimpleQueues plugin, String name, int size, Location tpLocation) {
		this.plugin = plugin;
		this.name = name;
		this.size = size;
		this.tpLocation = tpLocation;
		queueArray = new Player[size];
	}
	
	// Public function to return a string representation of the queue
	public String toString() {
		String repr = "";
		
		// Writes the list of players as a multiline string
		for (Player p : queueArray) {
			if (p == null) {
				repr += ("---\n");
			}
			else {
				repr += (p.getName() + "\n");
			}
		}
		
		return repr;
	}
	
	// Public function to try to add a player to the queue
	// returned int: 0=normal, 1=queueFull, 2=playerInQueue
	public int add(Player p) {
		int outcome = 2;
		
		// Test if the player is already in the queue
		for (Player p2 : queueArray) {
			if (p2 != null && p2.getName().equalsIgnoreCase(p.getName())) {
				return 1;
			}
		}
		
		// Calculate the next empty slot in the queue
		int nextEmpty = Helpers.nextEmpty(queueArray);
		
		// If the queue isn't full, add the player and mark as successful
		if (nextEmpty != -1) {
			queueArray[nextEmpty] = p;
			outcome = 0;
		}
		
		
		// If the queue is full, teleport everyone in the queue to tpLocation and reset the queue
		if (queueArray[queueArray.length - 1] != null) {
			plugin.getLogger().fine("Queue " + name + " full. Teleporting...");
			for (Player player : queueArray) {
				player.sendMessage("Queue " + name + " full. Teleporting...");
				player.teleport(tpLocation);
			}
			reset();
			plugin.getLogger().fine("The queue " + name + " has been reset.");
		}
		return outcome;
	}
	
	// Public function to try to remove a player from the queue
	public boolean remove(Player p) {
		// If the player is in the queue, find the player and set to null
		if (Arrays.asList(queueArray).contains(p)) {
			for (int pos = 0; pos < queueArray.length; pos++) {
				if (queueArray[pos].equals(p)) {
					queueArray[pos] = null;
					break;
				}
			}
			return true;
		}
		return false;
	}
	
	// Public function to reset the queue 
	public void reset() {
		// Set every value to null
		Arrays.fill(queueArray, null);
	}
}

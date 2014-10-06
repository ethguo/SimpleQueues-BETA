package com.github.ethg242.simplequeues;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.ethg242.simplequeues.misc.Helpers;
import com.github.ethg242.simplequeues.misc.MessageColor;

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
	public boolean add(Player p) {
		// Test if the player is already in the queue
		for (Player p2 : queueArray) {
			if (p2 != null && p2.getName().equalsIgnoreCase(p.getName())) {
				return false;
			}
		}
		
		// Calculate the next empty slot in the queue
		int nextEmpty = Helpers.nextEmpty(queueArray);
		
		// If the queue isn't full, add the player
		if (nextEmpty != -1) {
			queueArray[nextEmpty] = p;
		}
		
		// If the queue is full, teleport everyone in the queue to tpLocation and reset the queue
		if (Helpers.nextEmpty(queueArray) == -1) {
			plugin.getLogger().fine("Queue " + name + " full. Teleporting...");
			for (Player player : queueArray) {
				player.sendMessage(MessageColor.NORMAL + "Queue " + MessageColor.HIGHLIGHT + name + MessageColor.NORMAL + " full. Teleporting...");
				player.teleport(tpLocation);
				// Also remove each player from any other queue they may be in
				for (Queue queue : plugin.queues) {
					try {
						queue.remove(player);
					} catch (Exception e) { }
				}
			}
			reset();
			plugin.getLogger().fine("The queue " + name + " has been reset.");
		}
		return true;
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

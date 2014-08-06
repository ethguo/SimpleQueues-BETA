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
	
	// Shortcut to function below
	public void add(Player p) {
		add(p, null);
	}
	
	// Public function to try to add a player to the queue
	public void add(Player p, Player s) {
		boolean senderMode = false;
		if (s != null) {
			senderMode = true;
		}
		
		// Test if the player is already in the queue
		for (Player p2 : queueArray) {
			if (p2 != null && p2.getName().equalsIgnoreCase(p.getName())) {
				if (!senderMode) {
					p.sendMessage(MessageColor.PROBLEM + "You are already in the queue " + MessageColor.HIGHLIGHT + name);
				} else {
					s.sendMessage(MessageColor.HIGHLIGHT + p.getName() + MessageColor.PROBLEM + " is already in the queue " + MessageColor.HIGHLIGHT + name);
				}
				return;
			}
		}
		
		// Calculate the next empty slot in the queue
		int nextEmpty = Helpers.nextEmpty(queueArray);
		
		// If the queue isn't full, add the player and mark as successful
		if (nextEmpty != -1) {
			queueArray[nextEmpty] = p;
			
			if (!senderMode) {
				p.sendMessage(MessageColor.NORMAL + "You have been added to the queue " + MessageColor.HIGHLIGHT + name);
				plugin.getServer().getLogger().fine(p.getName() + " has been added to the queue " + name);
			} else {
				s.sendMessage(MessageColor.HIGHLIGHT + p.getName() + MessageColor.NORMAL + " has been added to the queue " + MessageColor.HIGHLIGHT + name);
				p.sendMessage(MessageColor.NORMAL + "You have been added to the queue " + MessageColor.HIGHLIGHT + name + MessageColor.NORMAL + " by " + MessageColor.HIGHLIGHT + s.getName());
				plugin.getServer().getLogger().fine(p.getName() + " has been added to the queue " + name + " by " + s.getName());
			}
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

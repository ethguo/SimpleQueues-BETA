package com.github.ethg242.simplequeues;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
	private SimpleQueues plugin;
	
	public EventListener(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		// If a player quits, remove them from every queue
		Player p = e.getPlayer();
		for (Queue queue : plugin.queues) {
			try {
				queue.remove(p);
			} catch (Exception ex) { }
		}
	}
}

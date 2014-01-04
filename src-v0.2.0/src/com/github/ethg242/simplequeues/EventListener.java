package com.github.ethg242.simplequeues;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
	private SimpleQueues plugin;
	
	public EventListener(SimpleQueues plugin) {
		this.plugin = plugin;
	}
	
	// If a player quits, remove them from every queue
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		for (Queue queue : plugin.queues) {
			try {
				queue.remove(p);
			} catch (Exception ex) { }
		}
	}
	
	// Formatting Action signs
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		
	}
	
	// Joining queues from signs
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material block = e.getClickedBlock().getType();
			if (block == Material.WALL_SIGN || block == Material.SIGN_POST) {
				// If a player right-clicks a block and that block is a sign (Wall Sign or Free-Standing Sign):
				Sign sign = (Sign)(e.getClickedBlock().getState());
				// If the sign's first line matches the format:
				if (sign.getLine(0).equalsIgnoreCase("[QUEUE]")) {
					// Try to get the queue by matching names
					Queue q = null;
					String queueName = sign.getLine(1);
					for (Queue queue : plugin.queues) {
						if (queueName.equals(queue.name)) {
							q = queue;
							break;
						}
					}
					if (q != null) {
						// If the queue was found, add the player to the queue.
						Player p = e.getPlayer();
						q.add(p);
						p.sendMessage("You have been added to the queue " + q.name);
						plugin.getServer().getLogger().fine(p.getName() + " has been added to the queue " + q.name);
					}
				}
			}
		}
	}
}

package com.github.ethg242.simplequeues.misc;

import org.bukkit.Location;
import org.bukkit.World;

import com.github.ethg242.simplequeues.SimpleQueues;

public class Helpers {
	public static int nextEmpty(Object[] array) {
		int nextEmpty = 0;
		for ( ; (nextEmpty < array.length) && (array[nextEmpty] != null); nextEmpty++) { }
		try {
			if (array[nextEmpty] != null) { return -1; }
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
		return nextEmpty;
	}
	
	// Public helper function to parse 4 strings into a Location
	public static Location parseLocation(SimpleQueues plugin, String x, String y, String z, String w) {
		// Convert each string coordinate to double
		double tpX;
		try { tpX = Double.parseDouble(x); } 
		catch (NumberFormatException e) {
			throw new LocationFormatException(x + " is not a valid coordinate.");
		}
		double tpY;
		try { tpY = Double.parseDouble(y); }
		catch (NumberFormatException e) {
			throw new LocationFormatException(y + " is not a valid coordinate.");
		}
		double tpZ;
		try { tpZ = Double.parseDouble(z); }
		catch (NumberFormatException e) {
			throw new LocationFormatException(z + " is not a valid coordinate.");
		}
		// Get the world
		World tpW = plugin.getServer().getWorld(w);
		if (tpW == null) {
			throw new LocationFormatException(w + " is not a valid world.");
		}
		
		// Assemble the Location object
		return new Location(tpW, tpX, tpY, tpZ);
	}
}

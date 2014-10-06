package com.github.ethg242.simplequeues.misc;

import org.bukkit.ChatColor;

public enum MessageColor {
	NORMAL("" + ChatColor.RESET + ChatColor.BLUE),
	HIGHLIGHT("" + ChatColor.RESET + ChatColor.AQUA),
	PROBLEM("" + ChatColor.RESET + ChatColor.DARK_AQUA),
	LISTHEAD("" + ChatColor.RESET + ChatColor.BLUE + ChatColor.BOLD),
	LISTBODY("" + ChatColor.RESET + ChatColor.AQUA),
	;
	
	private final String colorCode;
	
	private MessageColor(String colorCode) {
		this.colorCode = colorCode;
	}
	
	public String toString() {
		return colorCode;
	}
}

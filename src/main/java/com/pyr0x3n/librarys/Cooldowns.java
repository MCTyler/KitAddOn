package com.pyr0x3n.librarys;

import java.util.HashMap;

public class Cooldowns {
	private static HashMap<String, Long> coolD = new HashMap<String, Long>();

	public static long getCooldown(String playerName) {
		return calculateRemainder(coolD.get(playerName));
	}

	public static long setCooldown(String playerName, long delay) {
		return calculateRemainder(
				coolD.put(playerName, System.currentTimeMillis() + delay));
	}

	public static boolean tryCooldown(String playerName, long delay) {
		if (getCooldown(playerName) <= 0) {
			setCooldown(playerName, delay);
			return true;
		}
		return false;
	}

	public static void removeCooldowns(String playerName) {
		coolD.remove(playerName);  ;
	}

	private static long calculateRemainder(Long expireTime) {
		return expireTime != null ? expireTime - System.currentTimeMillis() : Long.MIN_VALUE;
	}
}

/** här är komanado för att ropa cooldown
	if (Cools.tryCooldown(player.getName(), 15000)) {
	    // Do what you need to do here. You don't have to set the cooldown when you're done.    	    	
	    } else {
	    // Cooldown hasn't expired yet
	    player.sendMessage("Du måste vänta " + (Cools.getCooldown(player.getName()) / 1000) + " sekunder till!");
	    }
	} 
 **/


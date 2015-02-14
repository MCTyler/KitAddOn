package com.pyr0x3n.librarys.Abilities;

import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pyr0x3n.librarys.Cooldowns;

public class Hulk  extends AbilityListener implements Disableable {
	public String cooldownMessage = ChatColor.BLUE + "You can use this again in %s seconds!";
	public int normalCooldown = 120;
	public int strengthTime = 10;
	public int strength = 0;
	

	public String hulkItemName = "Hulk Potion";
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().name().contains("RIGHT") && isSpecialItem(event.getItem(), hulkItemName)) {
			Player p = event.getPlayer();
			if (hasAbility(p)) {
				event.setCancelled(true);
				if (Cooldowns.tryCooldown(p.getName(), normalCooldown * 1000)) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthTime * 20, strength), true);
				}else {
					p.sendMessage( String.format(cooldownMessage,Cooldowns.getCooldown(p.getName()) / 1000));
					return;
				}
			}
		}
	}

}
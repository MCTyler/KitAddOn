package com.pyr0x3n.librarys.Abilities;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pyr0x3n.librarys.Cooldowns;

import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;

public class TimeLord extends AbilityListener implements Disableable {
	public int frozenTime = 5; 	
	public String FreezeMessage = ChatColor.GREEN + "You can't move for %s seconds the time has just stopped!";
	public String youStoppedTime = ChatColor.GREEN +"You've just stopped the time for %s seconds";
	public String cooldownMessage = ChatColor.BLUE + "You can use this again in %s seconds!";
	public int normalCooldown = 20;
	public double dist = 6;
	public String WatchItemName = "Stop Watch";


	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player p = event.getPlayer();

		if (event.getAction().name().contains("RIGHT") && isSpecialItem(event.getItem(), WatchItemName)) {
			if (hasAbility(p)) {
				if (Cooldowns.tryCooldown(p.getName(), normalCooldown * 1000)) {
					List<Entity> nearbyEntities = event.getPlayer().getNearbyEntities(dist, dist, dist);
					for (Entity target : nearbyEntities) {
						if ((target instanceof Player)) {
							Player targeted = (Player)target;
							targeted.sendMessage(String.format(FreezeMessage, frozenTime));
							p.sendMessage( String.format(youStoppedTime,frozenTime));
							targeted.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, frozenTime * 20, 200), true);
							targeted.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, frozenTime * 20, 200), true);    						
						}
					} 
				}else {
					p.sendMessage( String.format(cooldownMessage,Cooldowns.getCooldown(p.getName()) / 1000));
					return;
				}
			}
		}
	}



	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if (p.hasPotionEffect(PotionEffectType.SLOW) && p.hasPotionEffect(PotionEffectType.JUMP) ) {    			
				p.removePotionEffect(PotionEffectType.SLOW);
				p.removePotionEffect(PotionEffectType.JUMP);
			}
		}
	}


}

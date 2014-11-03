package com.pyr0x3n.librarys.Abilities;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;

public class Pirate extends AbilityListener implements Disableable {
	public String canonItemName = "Big Bertha";
	public int nauseaTime = 10; 
	public int nauseaDamage = 3;
	public int fireballDamage = 8;	

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction().name().contains("RIGHT") && isSpecialItem(event.getItem(), canonItemName)) {
			Player p = event.getPlayer();
			if (hasAbility(p)) {
				Fireball fireball = event.getPlayer().launchProjectile(Fireball.class);
				fireball.setIsIncendiary(false);
				fireball.setYield(0);
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, nauseaTime * 20, 2), true);
				p.damage((double) nauseaDamage);
			}
		}
	}



	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Fireball) {
			Fireball fireball = (Fireball) e.getDamager();

			if ( fireball.getShooter() instanceof Player) {
				Player shooter = (Player) fireball.getShooter();

				if (isSpecialItem(shooter.getItemInHand(), canonItemName)) {
					e.setDamage((double) fireballDamage);
				}
			}
		}
	}

}

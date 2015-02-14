package com.pyr0x3n.librarys.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class Anchor  extends AbilityListener implements Disableable {

	@EventHandler
	public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent event) {
		final Entity damaged = event.getEntity();
		final Entity damager = event.getDamager();


		//Anchor attacker
		if(damager instanceof Player){
			if (hasAbility((Player) damager)){										
				Bukkit.getScheduler().scheduleSyncDelayedTask(HungergamesApi.getHungergames(), new Runnable() {
					public void run() {
						damaged.setVelocity(new Vector(0,0,0));
						((Player) damager).playSound(damager.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						if(damaged instanceof Player){
							((Player) damaged).playSound(damaged.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						}
					}
				}, 1L);
			}
			//Anchor attacked
		}  
		if(damaged instanceof Player){
			if (hasAbility((Player) damaged)){
				Bukkit.getScheduler().scheduleSyncDelayedTask(HungergamesApi.getHungergames(), new Runnable() {
					public void run() {
						damaged.setVelocity(new Vector(0,0,0));
						((Player) damaged).playSound(damaged.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						if(damager instanceof Player){
							((Player) damager).playSound(damager.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
						}
					}
				}, 1L);
			}
		}

	}
	
}

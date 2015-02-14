package com.pyr0x3n.librarys.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;

public class Achilles  extends AbilityListener implements Disableable {
	public int damageDivisor = 4;
	public int woodSwordDamage = 9;
	private double damage;

	@EventHandler
	public void onEntityDamageEvent( EntityDamageEvent event) {
		if (event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if (hasAbility((player))){					
				damage = event.getDamage() / damageDivisor;
				event.setDamage((double) (damage));				
				if (event instanceof EntityDamageByEntityEvent){
					EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
					if( e.getDamager() instanceof Player){
						Player attacker = (Player) e.getDamager();
						if(attacker.getItemInHand().getType().equals(Material.WOOD_SWORD)){
							event.setDamage((double) woodSwordDamage);
						}
					}
				} 
			}				
		}
	}
}

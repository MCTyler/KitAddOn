package com.pyr0x3n.librarys.Abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.libraryaddict.Hungergames.Events.GameStartEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import me.libraryaddict.Hungergames.Types.Gamer;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class Champion extends AbilityListener implements Disableable{
	public int speed=1;
	public int fastDigging=1;
	public int jump=1;
	public int waterBreathing=1;
	public double damageMultiplicator = 2.0;
	
	
	@EventHandler
	public void onGameStartEvent(GameStartEvent event){
		for (Gamer gamer : HungergamesApi.getPlayerManager().getGamers()) {
			Player p =  (Player) gamer.getPlayer();			
			if (hasAbility(p)){
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, fastDigging), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, jump), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, waterBreathing), true);
			}
		}
	}
	
	
	@EventHandler
	public void onEntityDamageEvent( EntityDamageEvent event) {
		if (event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if (hasAbility((player))){	
				event.setDamage( event.getFinalDamage()*damageMultiplicator);
			}
		}
	}
}

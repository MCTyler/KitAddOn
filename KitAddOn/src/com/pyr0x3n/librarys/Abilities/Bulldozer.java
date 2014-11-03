package com.pyr0x3n.librarys.Abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.EventHandler;

import me.libraryaddict.Hungergames.Events.GameStartEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import me.libraryaddict.Hungergames.Types.Gamer;
import me.libraryaddict.Hungergames.Types.HungergamesApi;


public class Bulldozer extends AbilityListener implements Disableable {
	public int slowness=2;
	public int fastDigging=1;
	public int strength=0;

    @EventHandler
	public void onGameStartEvent(GameStartEvent event){
		for (Gamer gamer : HungergamesApi.getPlayerManager().getGamers()) {
			Player p =  (Player) gamer.getPlayer();			
			if (hasAbility(p)){
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, slowness), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, fastDigging), true);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, strength), true);
			}
		}
	}

}
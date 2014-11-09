package com.pyr0x3n.librarys.Abilities;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pyr0x3n.librarys.Area;

import me.libraryaddict.Hungergames.Events.InvincibilityWearOffEvent;
import me.libraryaddict.Hungergames.Events.TimeSecondEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import me.libraryaddict.Hungergames.Types.Gamer;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class ToxicTroop extends AbilityListener implements Disableable {
	public String iradiationMessage = ChatColor.RED + "DANGER! Radiation, back off!";	
	public String youIrradied = ChatColor.GREEN +"You've just irradied an %s blocks area.";
	public int halfLife = 10;
	public double irraditaionDamage= 8;
	public int radiationArea = 20;
	private Area area = new Area(radiationArea/2);
	private boolean invincibilityWearOff = false;


	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(!invincibilityWearOff) {
			if(event.getBlock().getType().equals(Material.DIAMOND_BLOCK)){
				event.setCancelled(true);	
				return;
			}
		}
		if(!event.getBlock().getType().equals(Material.DIAMOND_BLOCK)) return;
		Player p = event.getPlayer();
		if (hasAbility(p)) {
			area.doArea(event.getBlock().getLocation());
			p.sendMessage( String.format(youIrradied, radiationArea));
		}
	}
	
		
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().equals(Material.DIAMOND_BLOCK)){
			event.getBlock().getDrops().clear();
		}	
	}

	@EventHandler
	public  boolean onInvincibilityWearOffEvent(InvincibilityWearOffEvent event){
		return invincibilityWearOff= true;
	}
	
	
	@EventHandler
	public void onSecond(TimeSecondEvent event) {
		if(!invincibilityWearOff) return;
		if (HungergamesApi.getHungergames().currentTime % halfLife == 0) area.updateRadiationLevel();
		for (Gamer gamer : HungergamesApi.getPlayerManager().getGamers()) {	
			if (gamer.isSpectator()) continue;
			if (hasAbility(gamer.getPlayer())) continue;
			if(area.inArea(gamer.getPlayer())){
				gamer.getPlayer().sendMessage(iradiationMessage);
				gamer.getPlayer().setHealth(Math.max(
						gamer.getPlayer().getHealth()-
						(area.getRadiationLevel(gamer.getPlayer()) *irraditaionDamage), 0));
				gamer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 1));
			}
		}
	}		
}

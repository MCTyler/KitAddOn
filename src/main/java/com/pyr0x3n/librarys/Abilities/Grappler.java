package com.pyr0x3n.librarys.Abilities;

import java.util.Map;


//import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import com.pyr0x3n.librarys.MyFishingHook;

import me.libraryaddict.Hungergames.Events.PlayerKilledEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;

public class Grappler extends AbilityListener implements Disableable {
	private Map<Player, MyFishingHook> hooks = new java.util.HashMap<Player, MyFishingHook>();
	//public String cooldownMessage = ChatColor.BLUE + "You can use this again in %s seconds!";
	//public int normalCooldown = 20;
	public String HookItemName = "Grappling Hook";

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) { 
		if (this.hooks.containsKey(event.getPlayer())) {
			((MyFishingHook)this.hooks.get(event.getPlayer())).remove();
			this.hooks.remove(event.getPlayer());
		}
	}

	/**
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) { 
		if (!isSpecialItem(event.getPlayer().getItemInHand(), HookItemName)) {
			((MyFishingHook)this.hooks.get(event.getPlayer())).remove();
			this.hooks.remove(event.getPlayer());
		}
	}**/
    /**
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
    	if (this.hooks.containsKey(e.getPlayer()) && !isSpecialItem(e.getPlayer().getItemInHand(), HookItemName)){
    		((MyFishingHook)this.hooks.get(e.getPlayer())).remove();
    		this.hooks.remove(e.getPlayer());
    	}
    }**/
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event){
		Player p = event.getPlayer();
		if (isSpecialItem(event.getPlayer().getItemInHand(), HookItemName)) {
						
			event.setCancelled(true);
			event.getPlayer().updateInventory();
			event.setCancelled(true);

			if (!this.hooks.containsKey(p)) return;
			if (!((MyFishingHook)this.hooks.get(p)).isHooked()) { return;
			}
			double d = ((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().distance(p.getLocation());
			double t = d;
			double v_x = (1.0D + 0.07D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getX() 
					- p.getLocation().getX()) / t;
			double v_y = (1.0D + 0.03D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getY() 
					- p.getLocation().getY()) / t;
			double v_z = (1.0D + 0.07D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getZ() 
					- p.getLocation().getZ()) / t;

			Vector v = p.getVelocity();
			v.setX(v_x);
			v.setY(v_y);
			v.setZ(v_z);
			p.setVelocity(v);

			p.getWorld().playSound(p.getLocation(), Sound.STEP_GRAVEL, 0.0F, 0.0F);
		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if ((hasAbility(p)) && isSpecialItem(event.getPlayer().getItemInHand(), HookItemName) ){			
			event.setCancelled(true);
			if ((event.getAction() == 
					Action.LEFT_CLICK_AIR) || 
					(event.getAction() == 
					Action.LEFT_CLICK_BLOCK)) {
				if (this.hooks.containsKey(p)) {
					((MyFishingHook)this.hooks.get(p)).remove();
				}

				MyFishingHook nmsHook = new MyFishingHook(p.getWorld(), ((CraftPlayer)p).getHandle());
				nmsHook.spawn(p.getEyeLocation().add(p.getLocation().getDirection().getX(), 
						p.getLocation().getDirection().getY(), p.getLocation().getDirection().getZ()));
				nmsHook.move(p.getLocation().getDirection().getX() * 5.0D, 
						p.getLocation().getDirection().getY() * 5.0D, p.getLocation().getDirection().getZ() * 5.0D);
				this.hooks.put(p, nmsHook);
			}
			else {
				if (!this.hooks.containsKey(p))
					return;
				if (!((MyFishingHook)this.hooks.get(p)).isHooked()) {
					return;
				}

				double d = ((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().distance(p.getLocation());
				double t = d;
				double v_x = (1.2D + 0.07D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getX() 
						- p.getLocation().getX()) / t;
				double v_y = (1.0D + 0.05D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getY() 
						- p.getLocation().getY()) / t;
				double v_z = (1.2D + 0.07D * t) * (((MyFishingHook)this.hooks.get(p)).getBukkitEntity().getLocation().getZ() 
						- p.getLocation().getZ()) / t;

				Vector v = p.getVelocity();
				v.setX(v_x);
				v.setY(v_y);
				v.setZ(v_z);
				p.setVelocity(v);

				p.getWorld().playSound(p.getLocation(), Sound.STEP_GRAVEL, 10.0F, 10.0F);
			}
		}
	}
    @EventHandler
    public void onKilled(PlayerKilledEvent event) {
    	if (hooks.containsKey(event.getKilled().getPlayer())) {
    		((MyFishingHook) this.hooks.get(event.getKilled().getPlayer())).remove();
    		 this.hooks.remove(event.getKillerPlayer());
    	}
    }
    
    

    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    	if (hooks.containsKey(event.getPlayer())) {
    		hooks.remove(event.getPlayer());
    	}
    }

}

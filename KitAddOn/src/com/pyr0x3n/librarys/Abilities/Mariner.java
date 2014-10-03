package com.pyr0x3n.librarys.Abilities;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import me.libraryaddict.Hungergames.Events.PlayerKilledEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;

public class Mariner extends AbilityListener implements Disableable{
    private HashMap<Player, Boat> boats = new HashMap<Player, Boat>();
    public double boatSpeed = 0.5;
    public String BoatItemName = "HMS Endeavour";

    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
    	if (event.getAction().name().contains("RIGHT") && isSpecialItem(event.getItem(), BoatItemName)) {
    		Player p = event.getPlayer();
    		if (hasAbility(p)) {
    			if (boats.containsKey(p)) {
    				Boat boat = boats.remove(p);
    				boat.eject();
    				boat.leaveVehicle();
    				boat.remove();
    			}
    			Boat boat = (Boat) p.getWorld().spawnEntity(p.getLocation(), EntityType.BOAT);
    			boats.put(p, boat);
    			boat.setPassenger(p);
    			boat.setMaxSpeed(boatSpeed);
    			event.getPlayer().updateInventory();
    		}
    	}
    }

	@EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BOAT) {
            if (event.getWhoClicked().getVehicle() != null) {
                if (boats.containsValue(event.getWhoClicked().getVehicle())) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
    	if (!(event.getVehicle() instanceof Boat))  return;
    	Boat boat = (Boat) event.getVehicle();
        if (boats.containsValue(boat)) {
        	event.getVehicle().remove();
        	event.setCancelled(true);
            Iterator<Player> itel = boats.keySet().iterator();
            while (itel.hasNext()) {
                if (boats.get(itel.next()) == boat ) {
                    itel.remove();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent event) {
    	Boat boat = (Boat) event.getVehicle();
        if (boats.containsValue(boat)) {
        	event.getVehicle().remove();
        	event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onKilled(PlayerKilledEvent event) {
    	if (boats.containsKey(event.getKilled().getPlayer())) {
    		Boat boat = boats.remove(event.getKilled().getPlayer());
    		boat.eject();
    		boat.leaveVehicle();
    		boat.remove();
    	}
    }


}

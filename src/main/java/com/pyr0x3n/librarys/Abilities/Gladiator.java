package com.pyr0x3n.librarys.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.libraryaddict.Hungergames.Events.InvincibilityWearOffEvent;
import me.libraryaddict.Hungergames.Events.PlayerKilledEvent;
import me.libraryaddict.Hungergames.Interfaces.Disableable;
import me.libraryaddict.Hungergames.Types.AbilityListener;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class Gladiator  extends AbilityListener implements Disableable {

	private HashMap<Player, Location> origin = new HashMap<Player, Location>();
	private static HashMap<Player, Boolean > inbox = new HashMap<Player, Boolean>();
	private HashMap<Integer, ArrayList<Location>> blocks = new HashMap<Integer, ArrayList<Location>>();
	private boolean invincibilityWearOff= false;
	private HashMap<Player, Player> fighters =new HashMap<Player, Player>();
	private  int nextID = 0;
	private HashMap<Player, Integer> tasks = new HashMap<Player, Integer>();
	private HashMap<Integer, Player[]> players = new HashMap<Integer, Player[]>();
	public String freeSpaceAbove = ChatColor.BLUE + "Not enought free space above you to engage an 1 on 1 battle, try an other place.";
	public String killerMessage = ChatColor.GREEN + "You've a victory against %s!";
	public String invincibillityMessage = ChatColor.GREEN + "You have now %s seconds of invincibillity";
	public int battleDuration = 60;
	public int witherEffectDuration = 30;	
	public int invincibillity = 5;
	public int boxStartY = 40;
	public String gladiatorItemName ="The Shadow Game";
	
	public void log(String s, Level l){
		Bukkit.getServer().getLogger().log(l, "[Debug]" + s);
	}
	public void removeFighters(Player fighter1, Player fighter2){
		this.fighters.remove(fighter1);
		this.fighters.remove(fighter2);
	}
	
	public void addFighters(Player fighter1, Player fighter2){
		this.fighters.put(fighter1, fighter2);
		this.fighters.put(fighter2, fighter1);
	}
	
	@EventHandler
	public  boolean onInvincibilityWearOffEvent(InvincibilityWearOffEvent event){
		return invincibilityWearOff= true;
	}
	
	@EventHandler
    public void onClick(BlockPlaceEvent event) {
        if (isSpecialItem(event.getItemInHand(), gladiatorItemName)) {
        	event.setCancelled(true);
        }
    }
	
	public boolean isInTheBox(Entity entity){
		if (!(entity instanceof Player)) return false;
		Player player = (Player) entity;
		if (inbox.containsKey(player)){
			return inbox.get(player);	
		}
		return false;		
	}
	
	public boolean freeSpaceAbove(Player player){
		Location loc = player.getLocation();
		World world = loc.getWorld();
		int HighestBlockY = world.getHighestBlockYAt(loc);
		HighestBlockY += boxStartY;
		for (int x = 0; x <= 10; x++) {
			for(int z = 0; z <= 10; z++){
				for(int y = HighestBlockY; y <= 48; y++){
					Block block = loc.clone().add(x, y, z).getBlock();
					if(!block.equals(Material.AIR)){
						player.sendMessage(freeSpaceAbove);					
						return false;
					}
				}
			}
		}
		return true;	
	}
	
	public void buildThatBox(Location loc, int boxNunberb){
		final List<Location> theBox = new ArrayList<Location>();
		theBox.clear();
		for (int bX = 0; bX <= 10; bX++) {
			for (int bZ  = 0; bZ <= 10; bZ++) {
				for (int bY = 0; bY <= 8; bY++) {
					if (bY == 8) {
						theBox.add(loc.clone().add(bX, bY, bZ));
					} else if (bY == 0) {
						theBox.add(loc.clone().add(bX, bY, bZ));
					} else if ((bX == 0) || (bZ == 0) || (bX == 10) || (bZ == 10)) {
						theBox.add(loc.clone().add(bX, bY, bZ));
					}
				}
			}
		}
		for (Location blockLoc : theBox) {
			blockLoc.getBlock().setType(Material.GLASS);
		}
		blocks.put(boxNunberb, (ArrayList<Location>)theBox);
		
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(!invincibilityWearOff) return;
		if (isInTheBox(event.getRightClicked())) return;
		if (!hasAbility(event.getPlayer()) || !isSpecialItem(event.getPlayer().getItemInHand(), gladiatorItemName)) return;
		if (event.getRightClicked() instanceof Player) {
			if(!freeSpaceAbove(event.getPlayer())) return;
			final Player clicker =  event.getPlayer();
			final Player clicked = (Player) event.getRightClicked();
			origin.put(clicker, clicker.getLocation());
			origin.put(clicked, clicked.getLocation());			
			Location loc = clicked.getLocation().clone();
			loc.add(0, boxStartY, 0);

			// Teleport the players on corners and get them invisibiliry
			Location teleport = loc.clone();
			teleport.add(1, 2, 1);
			teleport.setYaw(-45.0F);
			clicked.teleport(teleport);
			inbox.put(clicked,true);
			teleport.add(8, 0, 8);
			teleport.setYaw(135.0F);
			clicker.teleport(teleport);
			clicker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,invincibillity * 20, 2));
			clicker.sendMessage( String.format(invincibillityMessage,invincibillity));
			clicked.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, invincibillity* 20, 2));
			clicked.sendMessage( String.format(invincibillityMessage,invincibillity));			
			inbox.put(clicker, true);
		     
			final Integer currentID = Integer.valueOf(this.nextID);
			this.nextID += 1;
		      ArrayList<Player> list = new ArrayList<Player>();
		      list.add(clicker);
		      list.add(clicked);

		      buildThatBox( loc, currentID);
		      addFighters(clicked, clicker);
			
			players.put(currentID, (Player[])list.toArray(new Player[1]));
			int id1 = Bukkit.getScheduler().scheduleSyncDelayedTask(HungergamesApi.getHungergames(), new Runnable() {
				public void run() {
					inbox.remove(clicker);
					inbox.remove(clicked);
					clicker.teleport(origin.get(clicker));
					clicked.teleport(origin.get(clicked));
					origin.remove(clicker);	
					origin.remove(clicked);
					for (Location loc : blocks.get(currentID)) {
						loc.getBlock().setType(Material.AIR);
					}
				}
			},(long) battleDuration * 20);
			
			int id2 = Bukkit.getScheduler().scheduleSyncDelayedTask(HungergamesApi.getHungergames(), new Runnable() {
				public void run() {
					clicker.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, witherEffectDuration * 20, 0));
					clicked.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, witherEffectDuration * 20, 0));
				}
			}, (long) battleDuration * 20);
			this.tasks.put(clicker, Integer.valueOf(id1));
			this.tasks.put(clicked, Integer.valueOf(id2));
		}
	}

	@EventHandler
	public void onPlayerKilled(PlayerKilledEvent event){
		if(!inbox.containsKey(event.getKilled().getPlayer())) return;
		//this.log(event.getEventName(), Level.INFO);
		//if(inbox.containsKey(event.getKilled())) return;
		//TODO set winner i quit
		Player killed = event.getKilled().getPlayer();
		//this.log("killed name " + event.getKilled().getPlayer().getName(), Level.INFO);		
		//this.log("killer name " + fighters.get(killed).getName(), Level.INFO);
		Player killer = fighters.get(killed);
		
		inbox.remove(killed);
		inbox.remove(killer);
		killer.teleport(origin.get(killer));
		killer.sendMessage( String.format(killerMessage, killed.getName()));
		killer.sendMessage( String.format(invincibillityMessage,invincibillity));
		killer.removePotionEffect(PotionEffectType.WITHER);
		killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, invincibillity * 20, 2));	

		Integer id = Integer.valueOf(-1);
		for (Map.Entry<Integer, Player[]> set : this.players.entrySet()){
			if ((((Player[])set.getValue())[0].equals(killer)) || 
					(((Player[])set.getValue())[0].equals(killed))) {
				id = (Integer)set.getKey();
			}
		}

		for (Location loc : blocks.remove(id)) {
			loc.getBlock().setType(Material.AIR);
		}
		Bukkit.getScheduler().cancelTask(((Integer)this.tasks.remove(killer)).intValue());
		Bukkit.getScheduler().cancelTask(((Integer)this.tasks.remove(killed)).intValue());
		
		World  dropWorld =origin.get(killer).getWorld();
		Location dropLoc =origin.get(killer);
		for (ItemStack stack : event.getDrops()) {
			dropWorld.dropItemNaturally(dropLoc, new ItemStack(stack));
		}

		
		
		event.getDrops().clear();
		origin.remove(killer);	
		origin.remove(killed);
		removeFighters(killer, killed);
		players.remove(id);
	}
	
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e){
		if (e.getBlock().getType().equals(Material.GLASS))  {
			if(!inbox.get(e.getPlayer())) return;
			e.setCancelled(true);
			final Block block = e.getBlock();
			block.setType(Material.BEDROCK);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HungergamesApi.getHungergames(), new Runnable(){
				public void run() {
					block.setType(Material.GLASS);
				}
			}, 2L);
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		Location loc = event.getLocation();
		Location locLow = loc.clone().add(0, -1, 0);
		Location locHigh = loc.clone().add(0, 7, 0);
		if(!locLow.getBlock().getType().equals(Material.GLASS)) return;
		if(!locHigh.getBlock().getType().equals(Material.GLASS)) return;
		event.blockList().clear();
	}
	

}
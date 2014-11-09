package com.pyr0x3n.librarys;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;



public class Area {

	private ConcurrentHashMap<Location, Double> locations =new ConcurrentHashMap<Location, Double>();
	private int radius;

	public Area( int radius){
		this.radius = radius;
	}

	public void doArea(Location loc){
		int bX =loc.getBlockX();
		int bY =loc.getBlockY();
		int bZ =loc.getBlockZ();

		for (int x= bX-radius; x <= bX+radius; x++){
			for (int y= bY-radius; y <= bY+radius; y++){
				for (int z= bZ-radius; z <= bZ+radius; z++){
					double dist = (((bX-x)*(bX-x))+((bY-y)*(bY-y))+((bZ-z)*(bZ-z)));
					if (dist < radius * radius){
						locations.put(new Location(loc.getWorld(),x,y,z) , 1.0);
					}
				}	
			}	
		}
	}

	public boolean inArea(Player player){
		Location loc = player.getLocation();
		loc.setX((int) player.getLocation().getBlockX());
		loc.setY((int) player.getLocation().getBlockY());
		loc.setZ((int) player.getLocation().getBlockZ());
		loc.setPitch(0);
		loc.setYaw(0);
		return locations.containsKey(loc);
	}

	public Double getRadiationLevel(Player player){
		Location loc = player.getLocation();
		loc.setX((int) player.getLocation().getBlockX());
		loc.setY((int) player.getLocation().getBlockY());
		loc.setZ((int) player.getLocation().getBlockZ());
		loc.setPitch(0);
		loc.setYaw(0);
		return locations.get(loc);	
	}


	public void updateRadiationLevel(){
		for (Map.Entry<Location, Double> entry : locations.entrySet()){
			if (entry.getValue()<0.005){
				locations.remove(entry.getKey());
				continue;
			}
			locations.put(entry.getKey(), entry.getValue()/2);
		}
	}



}

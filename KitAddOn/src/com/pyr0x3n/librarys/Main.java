package com.pyr0x3n.librarys;

import java.io.File;

import me.libraryaddict.Hungergames.Types.HungergamesApi;
import me.libraryaddict.Hungergames.Types.Kit;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
		
	@Override
	public void onEnable() {
		File file = new File(getDataFolder().toString() + "/kits.yml");
		ConfigurationSection config;
		if (!file.exists()) {
			saveResource("kits.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
        saveDefaultConfig();
        
		HungergamesApi.getAbilityManager().initializeAllAbilitiesInPackage(this, "com.pyr0x3n.librarys.Abilities");
		if (config.contains("Kits")) {
			for (String string : config.getConfigurationSection("Kits").getKeys(false)) {
				if (config.contains("BadKits") && config.getStringList("BadKits").contains(string))
					continue;
				Kit kit = HungergamesApi.getKitManager().parseKit(config.getConfigurationSection("Kits." + string));
				HungergamesApi.getKitManager().addKit(kit);
			}
		}
	}
	
}

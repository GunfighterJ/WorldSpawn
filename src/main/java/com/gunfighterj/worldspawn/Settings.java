package com.gunfighterj.worldspawn;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Settings
{
	private WorldSpawn worldSpawn;
	private Map<String, World> worldMap;

	public Settings(WorldSpawn plugin)
	{
		this.worldSpawn = plugin;
		worldMap = new HashMap<>();

		if (!new File(worldSpawn.getDataFolder(), "config.yml").exists())
		{
			worldSpawn.getLogger().info("Creating default config");
			worldSpawn.saveDefaultConfig();
		}
	}

	public void reload()
	{
		worldMap.clear();
		worldSpawn.reloadConfig();

		for (String worldMapping : worldSpawn.getConfig().getConfigurationSection("settings").getKeys(false))
		{
			String spawnWorld = worldSpawn.getConfig().getString("settings." + worldMapping + ".spawnWorld", worldSpawn.getServer().getWorlds().get(0).getName());
			World world = worldSpawn.getServer().getWorld(spawnWorld);
			for (String deathWorld : worldSpawn.getConfig().getStringList("settings." + worldMapping + ".deathWorlds"))
			{
				worldSpawn.getLogger().info("[WorldSpawn] Adding mapping: " + deathWorld + " => " + spawnWorld);
				worldMap.put(deathWorld.toLowerCase(), world);
			}
		}
	}

	public World getWorld(String deathWorld)
	{
		return worldMap.get(deathWorld.toLowerCase());
	}

}

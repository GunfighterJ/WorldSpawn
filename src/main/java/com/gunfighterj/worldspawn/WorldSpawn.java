package com.gunfighterj.worldspawn;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class WorldSpawn extends JavaPlugin implements Listener
{
	private Map<String, World> deathMap = new HashMap<>();
	private Settings settings;

	@Override
	public void onEnable()
	{
		settings = new Settings(this);
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{
		deathMap = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("worldspawn"))
		{
			if (sender.hasPermission("worldspawn.command"))
			{
				settings.reload();
				sender.sendMessage("WorldSpawn settings reloaded.");
			}
			else
			{
				sender.sendMessage("You do not have permission to run that command.");
			}
		}
		return true;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(EntityDeathEvent event)
	{
		if (!(event.getEntity() instanceof Player))
		{
			return;
		}

		String worldName = event.getEntity().getWorld().getName();
		deathMap.put(((Player) event.getEntity()).getName(), settings.getWorld(worldName));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		if (deathMap.containsKey(player.getName()))
		{
			World world = deathMap.remove(player.getName());
			if (world == null)
			{
				getLogger().warning("Something went wrong. Couldn't find spawnWorld specified in the config");
				return;
			}
			event.setRespawnLocation(world.getSpawnLocation());
		}
	}
}

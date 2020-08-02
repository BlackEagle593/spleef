package de.eaglefamily.minecraft.spleef.command;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpleefCommand implements CommandExecutor {

  private final SpawnpointPool spawnpointPool;

  @Inject
  public SpleefCommand(SpawnpointPool spawnpointPool) {
    this.spawnpointPool = spawnpointPool;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;

    if (args.length == 0) {
      return true;
    }

    switch (args[0].toLowerCase()) {
      case "spawn":
        createSpawnpoint(player);
      default:
        break;
    }

    return true;
  }

  private void createSpawnpoint(Player player) {
    spawnpointPool.addSpawnpoint(player.getLocation());
  }
}

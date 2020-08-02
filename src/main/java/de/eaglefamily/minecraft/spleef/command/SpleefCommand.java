package de.eaglefamily.minecraft.spleef.command;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.map.Arena;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpleefCommand implements CommandExecutor {

  private final SpawnpointPool spawnpointPool;
  private final Arena arena;

  @Inject
  public SpleefCommand(SpawnpointPool spawnpointPool, Arena arena) {
    this.spawnpointPool = spawnpointPool;
    this.arena = arena;
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
        break;
      case "block":
        addSpleefBlock(player, Arrays.copyOfRange(args, 1, args.length));
        break;
      default:
        break;
    }

    return true;
  }

  private void createSpawnpoint(Player player) {
    spawnpointPool.addSpawnpoint(player.getLocation());
  }

  private void addSpleefBlock(Player player, String[] args) {
    if (args.length != 6) {
      return;
    }

    try {
      Location firstLocation = new Location(player.getWorld(), Double.parseDouble(args[0]),
          Double.parseDouble(args[1]), Double.parseDouble(args[2]));
      Location secondLocation = new Location(player.getWorld(), Double.parseDouble(args[3]),
          Double.parseDouble(args[4]), Double.parseDouble(args[5]));

      arena.addSpleefableBlocks(firstLocation, secondLocation);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }
}

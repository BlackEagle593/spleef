package de.eaglefamily.minecraft.spleef.listener;

import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  private final SpleefPlayerPool spleefPlayerPool;
  private final SpawnpointPool spawnpointPool;

  public PlayerJoinListener(SpleefPlayerPool spleefPlayerPool, SpawnpointPool spawnpointPool) {
    this.spleefPlayerPool = spleefPlayerPool;
    this.spawnpointPool = spawnpointPool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage("");
    SpleefPlayer spleefPlayer = spleefPlayerPool.addPlayer(player);
    player.teleport(spawnpointPool.getRandomSpawnpoint());
  }
}

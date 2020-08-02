package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

  private final SpleefPlayerPool spleefPlayerPool;
  private final SpawnpointPool spawnpointPool;

  @Inject
  public PlayerRespawnListener(SpleefPlayerPool spleefPlayerPool, SpawnpointPool spawnpointPool) {
    this.spleefPlayerPool = spleefPlayerPool;
    this.spawnpointPool = spawnpointPool;
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    SpleefPlayer spleefPlayer = spleefPlayerPool.getSpleefPlayer(event.getPlayer());
    spleefPlayer.resetState();
    event.setRespawnLocation(spawnpointPool.getRandomSpawnpoint());
  }
}

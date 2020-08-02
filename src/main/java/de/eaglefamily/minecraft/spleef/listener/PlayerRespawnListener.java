package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

  private final SpawnpointPool spawnpointPool;

  @Inject
  public PlayerRespawnListener(SpawnpointPool spawnpointPool) {
    this.spawnpointPool = spawnpointPool;
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    event.setRespawnLocation(spawnpointPool.getRandomSpawnpoint());
  }
}

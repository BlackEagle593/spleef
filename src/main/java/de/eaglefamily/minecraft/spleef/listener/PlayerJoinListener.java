package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;
  private final SpawnpointPool spawnpointPool;

  @Inject
  public PlayerJoinListener(Translator translator, SpleefPlayerPool spleefPlayerPool,
      SpawnpointPool spawnpointPool) {
    this.translator = translator;
    this.spleefPlayerPool = spleefPlayerPool;
    this.spawnpointPool = spawnpointPool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage("");
    SpleefPlayer spleefPlayer = spleefPlayerPool.addPlayer(player);
    spleefPlayer.resetState();
    spleefPlayer.setupInventory();
    player.teleport(spawnpointPool.getRandomSpawnpoint());
    translator.broadcastMessage("playerjoin", player.getName());
  }
}

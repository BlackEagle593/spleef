package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.map.SpawnpointPool;
import de.eaglefamily.minecraft.spleef.repository.PlayerNameRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;
  private final SpawnpointPool spawnpointPool;
  private final PlayerNameRepository playerNameRepository;

  /**
   * Create an instance of player join listener.
   *
   * @param translator           the translator
   * @param spleefPlayerPool     the spleef player pool
   * @param spawnpointPool       the spawnpoint pool
   * @param playerNameRepository the player name repository
   */
  @Inject
  public PlayerJoinListener(Translator translator, SpleefPlayerPool spleefPlayerPool,
      SpawnpointPool spawnpointPool, PlayerNameRepository playerNameRepository) {
    this.translator = translator;
    this.spleefPlayerPool = spleefPlayerPool;
    this.spawnpointPool = spawnpointPool;
    this.playerNameRepository = playerNameRepository;
  }

  /**
   * Call the player join listener.
   *
   * @param event the player join event
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage("");
    playerNameRepository.setPlayerName(player.getUniqueId(), player.getName()).subscribe();
    SpleefPlayer spleefPlayer = spleefPlayerPool.addPlayer(player);
    spleefPlayer.resetState();
    spleefPlayer.setupInventory();
    player.teleport(spawnpointPool.getRandomSpawnpoint());
    translator.broadcastMessage("playerjoin", player.getName());
  }
}

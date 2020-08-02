package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.repository.SpleefStatsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;
  private final SpleefStatsRepository spleefStatsRepository;

  /**
   * Create an instance of player quit listener.
   *
   * @param translator            the translator
   * @param spleefPlayerPool      the spleef player pool
   * @param spleefStatsRepository the spleef stats repository
   */
  @Inject
  public PlayerQuitListener(Translator translator, SpleefPlayerPool spleefPlayerPool,
      SpleefStatsRepository spleefStatsRepository) {
    this.translator = translator;
    this.spleefPlayerPool = spleefPlayerPool;
    this.spleefStatsRepository = spleefStatsRepository;
  }

  /**
   * Call the player quit listener.
   *
   * @param event the player quit event
   */
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    event.setQuitMessage("");

    // Kill player on combat log
    if (player.hasMetadata(SpleefPlayer.SPLEEFED_METAKEY)) {
      player.setHealth(0);
    }

    SpleefPlayer spleefPlayer = spleefPlayerPool.removePlayer(player);
    spleefPlayer.getStats().subscribe(stats -> spleefStatsRepository.saveStats(stats).subscribe());

    translator.broadcastMessage("playerquit", player.getName());
  }
}

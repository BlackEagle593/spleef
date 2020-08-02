package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;

  @Inject
  public PlayerQuitListener(Translator translator, SpleefPlayerPool spleefPlayerPool) {
    this.translator = translator;
    this.spleefPlayerPool = spleefPlayerPool;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    event.setQuitMessage("");
    SpleefPlayer spleefPlayer = spleefPlayerPool.removePlayer(player);
    translator.broadcastMessage("playerquit", player.getName());
  }
}

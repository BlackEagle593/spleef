package de.eaglefamily.minecraft.spleef.listener;

import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

  private final SpleefPlayerPool spleefPlayerPool;

  public PlayerQuitListener(SpleefPlayerPool spleefPlayerPool) {
    this.spleefPlayerPool = spleefPlayerPool;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    event.setQuitMessage("");
    SpleefPlayer spleefPlayer = spleefPlayerPool.removePlayer(player);
  }
}

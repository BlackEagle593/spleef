package de.eaglefamily.minecraft.spleef.listener;

import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

  private final SpleefPlayerPool spleefPlayerPool;

  public PlayerJoinListener(SpleefPlayerPool spleefPlayerPool) {
    this.spleefPlayerPool = spleefPlayerPool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage("");
    SpleefPlayer spleefPlayer = spleefPlayerPool.addPlayer(player);
  }
}

package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

  private final Translator translator;

  @Inject
  public PlayerDeathListener(Translator translator) {
    this.translator = translator;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    event.setDeathMessage("");
    event.setKeepInventory(true);
    Player player = event.getEntity();
    translator.broadcastMessage("died", player.getName());
    player.spigot().respawn();
  }
}

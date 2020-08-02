package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.MetadataValue;

public class PlayerDeathListener implements Listener {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;

  @Inject
  public PlayerDeathListener(Translator translator, SpleefPlayerPool spleefPlayerPool) {
    this.translator = translator;
    this.spleefPlayerPool = spleefPlayerPool;
  }

  /**
   * Call the player death listener. It detects if the player got spleefed or just died by himself.
   *
   * @param event the player death event
   */
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    event.setDeathMessage("");
    event.setKeepInventory(true);

    Player player = event.getEntity();
    if (player.hasMetadata(SpleefPlayer.SPLEEFED_METAKEY)) {
      whenSpleefed(player);
    } else {
      whenDied(player);
    }

    spleefPlayerPool.getSpleefPlayer(player).addDeath();

    player.spigot().respawn();
  }

  private void whenSpleefed(Player player) {
    MetadataValue metadataValue = player.getMetadata(SpleefPlayer.SPLEEFED_METAKEY).get(0);
    Player spleefer = (Player) metadataValue.value();
    SpleefPlayer spleeferPlayer = spleefPlayerPool.getSpleefPlayer(spleefer);
    if (Objects.isNull(spleefer) || !spleefer.isOnline() || Objects.isNull(spleeferPlayer)) {
      whenDied(player);
      return;
    }

    translator.sendMessage(player, "spleefed", player.getName(), spleefer.getName());
    translator.sendMessage(spleefer, "spleef", player.getName(), spleefer.getName());
    spleeferPlayer.addKill();
  }

  private void whenDied(Player player) {
    translator.sendMessage(player, "died", player.getName());
  }
}

package de.eaglefamily.minecraft.spleef.listener;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import java.util.Objects;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerMoveListener implements Listener {

  private final Plugin plugin;

  @Inject
  public PlayerMoveListener(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Call the player move listener. It checks, if the player is falling through a spleefable block.
   *
   * @param event the player move event
   */
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (event.getFrom().getBlockY() == event.getTo().getBlockY()) {
      return;
    }

    Block block = event.getTo().getBlock();
    if (!block.hasMetadata(SpleefPlayer.DESTROYED_METAKEY)) {
      return;
    }

    MetadataValue metadataValue = block.getMetadata(SpleefPlayer.DESTROYED_METAKEY).get(0);
    Player spleefer = (Player) metadataValue.value();
    if (Objects.isNull(spleefer)) {
      return;
    }

    Player player = event.getPlayer();
    if (player == spleefer) {
      return;
    }

    player.setMetadata(SpleefPlayer.SPLEEFED_METAKEY, new FixedMetadataValue(plugin, spleefer));
  }
}

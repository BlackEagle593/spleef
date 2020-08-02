package de.eaglefamily.minecraft.spleef.listener;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.map.Arena;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@Singleton
public class BlockBreakListener implements Listener {

  private static final long MAX_BLOCK_RESPAWN_DELAY_IN_SECONDS = 30;
  private static final long MIN_BLOCK_RESPAWN_DELAY_IN_SECONDS = 5;

  private final Plugin plugin;
  private final Arena arena;
  private int destroyedBlockCount = 0;
  private final Map<Integer, BukkitRunnable> pendingTasks = Maps.newHashMap();

  @Inject
  public BlockBreakListener(Plugin plugin, Arena arena) {
    this.plugin = plugin;
    this.arena = arena;
  }

  /**
   * Call the block break listener. It avoids breaking non spleefable blocks and respawns broken
   * blocks after a certain delay.
   *
   * @param event the block break event
   */
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    if (arena.isSpleefable(block)) {
      event.setDropItems(false);
      destroyedBlockCount++;
      block.setMetadata(SpleefPlayer.DESTROYED_METAKEY,
          new FixedMetadataValue(plugin, event.getPlayer()));
      scheduleBlockRespawn(block);
      return;
    }

    event.setCancelled(true);
  }

  private void scheduleBlockRespawn(Block block) {
    final Material previousMaterial = block.getType();
    BukkitRunnable bukkitRunnable = new BukkitRunnable() {
      @Override
      public void run() {
        block.setType(previousMaterial);
        destroyedBlockCount--;
        pendingTasks.remove(getTaskId());
      }
    };

    bukkitRunnable.runTaskLater(plugin, calculateBlockRespawnDelay());
    pendingTasks.put(bukkitRunnable.getTaskId(), bukkitRunnable);
  }

  private long calculateBlockRespawnDelay() {
    float blocksLeftPercentage = 1 - destroyedBlockCount / (float) arena.getSpleefableBlockCount();
    return (long) (20 * (MAX_BLOCK_RESPAWN_DELAY_IN_SECONDS - MIN_BLOCK_RESPAWN_DELAY_IN_SECONDS)
        * blocksLeftPercentage) + 20 * MIN_BLOCK_RESPAWN_DELAY_IN_SECONDS;
  }

  public void restoreBlocks() {
    List.copyOf(pendingTasks.values()).forEach(Runnable::run);
  }
}

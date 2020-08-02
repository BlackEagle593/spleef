package de.eaglefamily.minecraft.spleef.map;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.eaglefamily.minecraft.spleef.util.BukkitRxWorker;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

@Singleton
public class Arena {

  public static final String SPLEEFBLOCKS_CONFIG_KEY = "spleefblocks";

  private final Plugin plugin;
  private final Configuration config;
  private final BukkitRxWorker bukkitRxWorker;
  private final List<Block> spleefableBlocks;

  /**
   * Create an instance of arena. The arena is responsible for the spleefable blocks.
   *
   * @param config         the config
   * @param plugin         the plugin
   * @param bukkitRxWorker the bukkit rx worker
   */
  @Inject
  public Arena(Configuration config, Plugin plugin, BukkitRxWorker bukkitRxWorker) {
    this.plugin = plugin;
    this.config = config;
    this.bukkitRxWorker = bukkitRxWorker;

    List<Location> configSpleefBlocks = (List<Location>) config.getList(SPLEEFBLOCKS_CONFIG_KEY,
        Lists.newArrayList());
    checkNotNull(configSpleefBlocks);
    spleefableBlocks = configSpleefBlocks.stream()
        .map(Location::getBlock)
        .collect(Collectors.toList());
  }

  /**
   * Check if the block is spleefable.
   *
   * @param block the block to check
   * @return true if the block is spleefable
   */
  public boolean isSpleefable(Block block) {
    return spleefableBlocks.contains(block);
  }

  /**
   * Gets the total count of spleefable blocks.
   *
   * @return the total count of spleefable blocks.
   */
  public int getSpleefableBlockCount() {
    return spleefableBlocks.size();
  }

  /**
   * Add multiple blocks to the spleefable blocks in the range between the first and second
   * location.
   *
   * @param firstLocation  the first location
   * @param secondLocation the second location
   */
  public void addSpleefableBlocks(Location firstLocation, Location secondLocation) {
    Single.fromCallable(() -> extractBlocks(firstLocation, secondLocation))
        .observeOn(Schedulers.computation())
        .subscribeOn(bukkitRxWorker.getScheduler())
        .subscribe(blocks -> blocks.forEach(this::addSpleefableBlock));
  }

  /**
   * Add a block to the spleefable blocks.
   *
   * @param block the block
   */
  public void addSpleefableBlock(Block block) {
    if (block.isEmpty() || spleefableBlocks.contains(block)) {
      return;
    }

    spleefableBlocks.add(block);
    config.set(SPLEEFBLOCKS_CONFIG_KEY,
        spleefableBlocks.stream().map(Block::getLocation).collect(Collectors.toList()));
    plugin.saveConfig();
  }

  private List<Block> extractBlocks(Location firstLocation, Location secondLocation) {
    int minX = Math.min(firstLocation.getBlockX(), secondLocation.getBlockX());
    int minY = Math.min(firstLocation.getBlockY(), secondLocation.getBlockY());
    int minZ = Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ());
    int maxX = Math.max(firstLocation.getBlockX(), secondLocation.getBlockX());
    int maxY = Math.max(firstLocation.getBlockY(), secondLocation.getBlockY());
    int maxZ = Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());

    List<Block> blocks = Lists.newArrayList();
    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {
        for (int z = minZ; z <= maxZ; z++) {
          Block block = new Location(firstLocation.getWorld(), x, y, z).getBlock();
          blocks.add(block);
        }
      }
    }

    return blocks;
  }
}

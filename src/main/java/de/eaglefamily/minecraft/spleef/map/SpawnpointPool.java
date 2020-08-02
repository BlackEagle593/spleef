package de.eaglefamily.minecraft.spleef.map;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

@Singleton
public class SpawnpointPool {

  public static final String SPAWNPOINT_CONFIG_KEY = "spawnpoints";

  private final Plugin plugin;
  private final Configuration config;
  private final List<Location> spawnpoints;
  private final Random random = new Random();

  @Inject
  public SpawnpointPool(Plugin plugin, Configuration config) {
    this.plugin = plugin;
    this.config = config;
    spawnpoints = (List<Location>) config.getList(SPAWNPOINT_CONFIG_KEY, Lists.newArrayList());
  }

  public Location getRandomSpawnpoint() {
    checkSpawnpoints();
    int randomInt = random.nextInt(spawnpoints.size());
    return spawnpoints.get(randomInt);
  }

  private void checkSpawnpoints() {
    checkState(!spawnpoints.isEmpty(), "No spawnpoints loaded! Further setup is required.");
  }

  public void addSpawnpoint(Location location) {
    if (spawnpoints.contains(location)) {
      return;
    }

    spawnpoints.add(location);
    config.set(SPAWNPOINT_CONFIG_KEY, spawnpoints);
    plugin.saveConfig();
  }
}

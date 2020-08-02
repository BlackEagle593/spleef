package de.eaglefamily.minecraft.spleef;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.repository.SpleefStatsRepository;
import de.eaglefamily.minecraft.spleef.util.BukkitRxWorker;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Singleton
public class SpleefPlayerPool {

  private final Plugin plugin;
  private final Translator translator;
  private final SpleefItemFactory spleefItemFactory;
  private final SpleefStatsRepository spleefStatsRepository;
  private final BukkitRxWorker bukkitRxWorker;
  private final Map<Player, SpleefPlayer> spleefPlayerMap = Maps.newConcurrentMap();

  /**
   * Create an instance of spleef player pool.
   *
   * @param plugin                the plugin
   * @param translator            the translator
   * @param spleefItemFactory     the spleef item factory
   * @param spleefStatsRepository the spleef stats repository
   * @param bukkitRxWorker        the bukkit rx worker
   */
  @Inject
  public SpleefPlayerPool(Plugin plugin, Translator translator, SpleefItemFactory spleefItemFactory,
      SpleefStatsRepository spleefStatsRepository, BukkitRxWorker bukkitRxWorker) {
    this.plugin = plugin;
    this.translator = translator;
    this.spleefItemFactory = spleefItemFactory;
    this.spleefStatsRepository = spleefStatsRepository;
    this.bukkitRxWorker = bukkitRxWorker;
  }

  public SpleefPlayer getSpleefPlayer(Player player) {
    return spleefPlayerMap.get(player);
  }

  /**
   * Add a player to the pool.
   *
   * @param player the player to add
   * @return the spleef player which was added
   */
  public SpleefPlayer addPlayer(Player player) {
    checkNotNull(player);
    SpleefPlayer spleefPlayer = SpleefPlayer.create(player, plugin, translator, spleefItemFactory,
        spleefStatsRepository, bukkitRxWorker);
    spleefPlayerMap.put(player, spleefPlayer);
    return spleefPlayer;
  }

  /**
   * Remove a player from the pool.
   *
   * @param player the player to remove
   * @return the spleef player which was removed
   */
  public SpleefPlayer removePlayer(Player player) {
    checkNotNull(player);
    return spleefPlayerMap.remove(player);
  }
}

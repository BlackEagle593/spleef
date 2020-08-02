package de.eaglefamily.minecraft.spleef;

import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.repository.SpleefStatsRepository;
import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import de.eaglefamily.minecraft.spleef.util.BukkitRxWorker;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Objects;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class SpleefPlayer {

  public static final String DESTROYED_METAKEY = "destroyedBy";
  public static final String SPLEEFED_METAKEY = "spleefedBy";
  private static final List<Integer> killstreakBroadcasts = List.of(5, 10, 20, 50, 100);

  private final Player player;
  private final Plugin plugin;
  private final Translator translator;
  private final SpleefItemFactory spleefItemFactory;

  private final Stats sessionStats;
  private Stats globalStats;
  private final Single<Stats> statsLoaderTask;

  private SpleefPlayer(Player player, Plugin plugin, Translator translator,
      SpleefItemFactory spleefItemFactory, SpleefStatsRepository spleefStatsRepository,
      BukkitRxWorker bukkitRxWorker) {
    this.player = player;
    this.plugin = plugin;
    this.translator = translator;
    this.spleefItemFactory = spleefItemFactory;

    sessionStats = Stats.empty(player.getUniqueId());
    statsLoaderTask = spleefStatsRepository.getStats(player.getUniqueId())
        .defaultIfEmpty(Stats.empty(player.getUniqueId()))
        .subscribeOn(bukkitRxWorker.getScheduler());
    statsLoaderTask.subscribe(stats -> globalStats = stats);
  }

  static SpleefPlayer create(Player player, Plugin plugin, Translator translator,
      SpleefItemFactory spleefItemFactory, SpleefStatsRepository spleefStatsRepository,
      BukkitRxWorker bukkitRxWorker) {
    return new SpleefPlayer(player, plugin, translator, spleefItemFactory, spleefStatsRepository,
        bukkitRxWorker);
  }

  public void resetState() {
    AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    player.setHealth(Objects.nonNull(maxHealth) ? maxHealth.getValue() : 20);
    player.setLevel(0);
    player.removeMetadata(SPLEEFED_METAKEY, plugin);
  }

  public void setupInventory() {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    ItemStack shovel = spleefItemFactory.createShovel();
    inventory.setItem(0, shovel);
  }

  public void addKill() {
    int killstreak = player.getLevel() + 1;
    player.setLevel(killstreak);
    if (killstreakBroadcasts.contains(killstreak)) {
      translator.broadcastMessage("killstreak", player.getName(), killstreak);
    }

    sessionStats.incrementKills();
  }

  public void addDeath() {
    sessionStats.incrementDeaths();
  }

  public Single<Stats> getStats() {
    if (Objects.nonNull(globalStats)) {
      return Single.just(Stats.combined(sessionStats, globalStats));
    }

    return statsLoaderTask;
  }
}

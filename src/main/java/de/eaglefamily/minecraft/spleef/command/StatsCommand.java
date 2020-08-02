package de.eaglefamily.minecraft.spleef.command;

import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.SpleefPlayer;
import de.eaglefamily.minecraft.spleef.SpleefPlayerPool;
import de.eaglefamily.minecraft.spleef.i18n.Translator;
import de.eaglefamily.minecraft.spleef.repository.PlayerNameRepository;
import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import de.eaglefamily.minecraft.spleef.util.BukkitRxWorker;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

  private final Translator translator;
  private final SpleefPlayerPool spleefPlayerPool;
  private final PlayerNameRepository playerNameRepository;
  private final BukkitRxWorker bukkitRxWorker;

  /**
   * Create an instance of stats command.
   *
   * @param translator           the translator
   * @param spleefPlayerPool     the spleef player pool
   * @param playerNameRepository the plyar name repository
   * @param bukkitRxWorker       the bukkit rx worker
   */
  @Inject
  public StatsCommand(Translator translator, SpleefPlayerPool spleefPlayerPool,
      PlayerNameRepository playerNameRepository, BukkitRxWorker bukkitRxWorker) {
    this.translator = translator;
    this.bukkitRxWorker = bukkitRxWorker;
    this.spleefPlayerPool = spleefPlayerPool;
    this.playerNameRepository = playerNameRepository;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    SpleefPlayer spleefPlayer = spleefPlayerPool.getSpleefPlayer(player);
    Disposable statsDisposable = spleefPlayer.getStats()
        .subscribeOn(bukkitRxWorker.getScheduler())
        .timeout(10, TimeUnit.SECONDS)
        .subscribe(stats -> printStats(player, stats), error -> printStatsError(player));

    // display loading message if stats are not loaded after one second
    Completable.timer(1, TimeUnit.SECONDS)
        .subscribeOn(bukkitRxWorker.getScheduler())
        .subscribe(() -> {
          if (!statsDisposable.isDisposed()) {
            printStatsLoading(player);
          }
        });

    return true;
  }

  private void printStats(Player player, Stats stats) {
    Player statsPlayer = Bukkit.getPlayer(stats.getUniqueId());
    if (Objects.nonNull(statsPlayer)) {
      printStats(player, stats, statsPlayer.getName());
    } else {
      playerNameRepository.getPlayerName(stats.getUniqueId())
          .subscribeOn(bukkitRxWorker.getScheduler())
          .subscribe(name -> printStats(player, stats, name.getName()));
    }
  }

  private void printStats(Player player, Stats stats, String name) {
    translator.sendMessage(player, "stats", stats.getUniqueId(), name, stats.getKills(),
        stats.getDeaths(), stats.getKillDeathRatio());
  }

  private void printStatsLoading(Player player) {
    translator.sendMessage(player, "stats.loading");
  }

  private void printStatsError(Player player) {
    translator.sendMessage(player, "stats.error");
  }
}

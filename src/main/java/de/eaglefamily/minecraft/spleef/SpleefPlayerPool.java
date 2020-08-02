package de.eaglefamily.minecraft.spleef;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Singleton
public class SpleefPlayerPool {

  private final Plugin plugin;
  private final SpleefItemFactory spleefItemFactory;
  private final Map<Player, SpleefPlayer> spleefPlayerMap = Maps.newConcurrentMap();

  @Inject
  public SpleefPlayerPool(Plugin plugin, SpleefItemFactory spleefItemFactory) {
    this.plugin = plugin;
    this.spleefItemFactory = spleefItemFactory;
  }

  public SpleefPlayer getSpleefPlayer(Player player) {
    return spleefPlayerMap.get(player);
  }

  public SpleefPlayer addPlayer(Player player) {
    checkNotNull(player);
    SpleefPlayer spleefPlayer = SpleefPlayer.create(player, plugin, spleefItemFactory);
    spleefPlayerMap.put(player, spleefPlayer);
    return spleefPlayer;
  }

  public SpleefPlayer removePlayer(Player player) {
    checkNotNull(player);
    return spleefPlayerMap.remove(player);
  }
}

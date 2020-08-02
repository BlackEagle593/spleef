package de.eaglefamily.minecraft.spleef;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.entity.Player;

public class SpleefPlayerPool {

  private final Map<Player, SpleefPlayer> spleefPlayerMap = Maps.newConcurrentMap();

  public SpleefPlayer getSpleefPlayer(Player player) {
    return spleefPlayerMap.get(player);
  }

  public SpleefPlayer addPlayer(Player player) {
    checkNotNull(player);
    SpleefPlayer spleefPlayer = SpleefPlayer.create(player);
    spleefPlayerMap.put(player, spleefPlayer);
    return spleefPlayer;
  }

  public SpleefPlayer removePlayer(Player player) {
    checkNotNull(player);
    return spleefPlayerMap.remove(player);
  }
}

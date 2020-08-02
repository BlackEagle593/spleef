package de.eaglefamily.minecraft.spleef;

import org.bukkit.entity.Player;

public class SpleefPlayer {

  public static final String DESTROYED_METAKEY = "destroyedBy";

  private final Player player;

  private SpleefPlayer(Player player) {
    this.player = player;
  }

  static SpleefPlayer create(Player player) {
    return new SpleefPlayer(player);
  }
}

package de.eaglefamily.minecraft.spleef;

import org.bukkit.entity.Player;

public class SpleefPlayer {

  private final Player player;

  private SpleefPlayer(Player player) {
    this.player = player;
  }

  static SpleefPlayer create(Player player) {
    return new SpleefPlayer(player);
  }
}

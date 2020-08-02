package de.eaglefamily.minecraft.spleef;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SpleefPlayer {

  public static final String DESTROYED_METAKEY = "destroyedBy";

  private final Player player;
  private final SpleefItemFactory spleefItemFactory;

  private SpleefPlayer(Player player, SpleefItemFactory spleefItemFactory) {
    this.player = player;
    this.spleefItemFactory = spleefItemFactory;
  }

  static SpleefPlayer create(Player player, SpleefItemFactory spleefItemFactory) {
    return new SpleefPlayer(player, spleefItemFactory);
  }

  public void setupInventory() {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    ItemStack shovel = spleefItemFactory.createShovel();
    inventory.setItem(0, shovel);
  }
}

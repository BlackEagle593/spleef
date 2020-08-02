package de.eaglefamily.minecraft.spleef;

import java.util.Objects;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

  public void resetState() {
    AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    player.setHealth(Objects.nonNull(maxHealth) ? maxHealth.getValue() : 20);
  }

  public void setupInventory() {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    ItemStack shovel = spleefItemFactory.createShovel();
    inventory.setItem(0, shovel);
  }
}

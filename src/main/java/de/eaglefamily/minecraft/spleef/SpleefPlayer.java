package de.eaglefamily.minecraft.spleef;

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

  private final Player player;
  private final Plugin plugin;
  private final SpleefItemFactory spleefItemFactory;

  private SpleefPlayer(Player player, Plugin plugin, SpleefItemFactory spleefItemFactory) {
    this.player = player;
    this.plugin = plugin;
    this.spleefItemFactory = spleefItemFactory;
  }

  static SpleefPlayer create(Player player, Plugin plugin, SpleefItemFactory spleefItemFactory) {
    return new SpleefPlayer(player, plugin, spleefItemFactory);
  }

  public void resetState() {
    AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    player.setHealth(Objects.nonNull(maxHealth) ? maxHealth.getValue() : 20);
    player.removeMetadata(SPLEEFED_METAKEY, plugin);
  }

  public void setupInventory() {
    PlayerInventory inventory = player.getInventory();
    inventory.clear();
    ItemStack shovel = spleefItemFactory.createShovel();
    inventory.setItem(0, shovel);
  }

  public void addKill() {
    // TODO - add kill to stats
    // TODO - implement killstreak logic
  }

  public void addDeath() {
    // TODO - add death to stats
  }
}

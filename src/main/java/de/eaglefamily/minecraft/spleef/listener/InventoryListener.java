package de.eaglefamily.minecraft.spleef.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InventoryListener implements Listener {

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onCraftItem(CraftItemEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    event.setCancelled(true);
  }
}

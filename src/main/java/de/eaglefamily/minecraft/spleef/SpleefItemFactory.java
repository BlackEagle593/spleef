package de.eaglefamily.minecraft.spleef;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpleefItemFactory {

  /**
   * Create shovel to spleef snow.
   *
   * @return the itemstack of the shovel
   */
  public ItemStack createShovel() {
    ItemStack item = new ItemStack(Material.IRON_SHOVEL);
    ItemMeta meta = item.getItemMeta();
    meta.setUnbreakable(true);
    meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
    item.setItemMeta(meta);
    return item;
  }
}

package de.eaglefamily.minecraft.spleef.i18n;

import org.bukkit.entity.Player;

public interface Translator {

  String translate(Player player, String key, Object... arguments);

  void sendMessage(Player player, String key, Object... arguments);

  void broadcastMessage(String key, Object... arguments);
}

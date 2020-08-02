package de.eaglefamily.minecraft.spleef.i18n;

import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageFormatTranslator implements Translator {

  private final Map<Locale, ResourceBundle> cachedBundles = Maps.newHashMap();
  private final Map<String, MessageFormat> messageFormats = Maps.newHashMap();

  @Override
  public String translate(Player player, String key, Object... arguments) {
    Locale locale = Locale.forLanguageTag(player.getLocale());
    ResourceBundle bundle = getBundle(locale);
    MessageFormat messageFormat = getMessageFormat(bundle.getString(key), locale);
    return messageFormat.format(arguments);
  }

  @Override
  public void sendMessage(Player player, String key, Object... arguments) {
    player.sendMessage(translate(player, key, arguments));
  }

  @Override
  public void broadcastMessage(String key, Object... arguments) {
    Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, key, arguments));
  }

  private ResourceBundle getBundle(Locale locale) {
    return cachedBundles.computeIfAbsent(locale,
        key -> ResourceBundle.getBundle("spleef", locale));
  }

  private MessageFormat getMessageFormat(final String pattern, final Locale locale) {
    return messageFormats.computeIfAbsent(pattern, key -> new MessageFormat(key, locale));
  }
}

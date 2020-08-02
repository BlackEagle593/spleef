package de.eaglefamily.minecraft.spleef;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.eaglefamily.minecraft.spleef.listener.PlayerJoinListener;
import de.eaglefamily.minecraft.spleef.listener.PlayerQuitListener;
import java.util.List;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpleefPlugin extends JavaPlugin implements Module {

  private final List<Class<? extends Listener>> listeners = List.of(PlayerJoinListener.class,
      PlayerQuitListener.class);

  private Injector injector;

  @Override
  public void configure(Binder binder) {
    binder.bind(Plugin.class).toInstance(this);
    binder.bind(Configuration.class).toInstance(getConfig());
  }

  @Override
  public void onEnable() {
    injector = Guice.createInjector(this);

    registerListeners();
  }

  private void registerListeners() {
    listeners.forEach(this::registerListenerClass);
  }

  private <T extends Listener> void registerListenerClass(Class<T> listener) {
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(injector.getInstance(listener), this);
  }
}

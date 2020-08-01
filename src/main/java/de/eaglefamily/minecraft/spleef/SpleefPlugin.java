package de.eaglefamily.minecraft.spleef;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpleefPlugin extends JavaPlugin implements Module {

  private Injector injector;

  @Override
  public void configure(Binder binder) {
    binder.bind(Plugin.class).toInstance(this);
    binder.bind(Configuration.class).toInstance(getConfig());
  }

  @Override
  public void onEnable() {
    injector = Guice.createInjector(this);
  }
}

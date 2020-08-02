package de.eaglefamily.minecraft.spleef.util;

import com.google.inject.Inject;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitRxWorker extends Scheduler.Worker {

  private final Plugin plugin;
  private final Scheduler scheduler;
  private final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Inject
  public BukkitRxWorker(Plugin plugin) {
    this.plugin = plugin;
    scheduler = new Scheduler() {
      @Override
      public Worker createWorker() {
        return new BukkitRxWorker(plugin);
      }
    };
  }

  @Override
  public synchronized Disposable schedule(Runnable run, long delay, TimeUnit unit) {
    if (compositeDisposable.isDisposed()) {
      return Disposable.disposed();
    }

    int taskId = bukkitScheduler.scheduleSyncDelayedTask(plugin, run,
        Math.round(unit.toMillis(delay) * 0.02));
    if (taskId < 0) {
      return Disposable.disposed();
    }

    Disposable disposable = new Disposable() {
      @Override
      public void dispose() {
        bukkitScheduler.cancelTask(taskId);
      }

      @Override
      public boolean isDisposed() {
        return !(bukkitScheduler.isQueued(taskId) || bukkitScheduler.isCurrentlyRunning(taskId));
      }
    };

    compositeDisposable.add(disposable);
    return disposable;
  }

  @Override
  public synchronized void dispose() {
    compositeDisposable.dispose();
  }

  @Override
  public synchronized boolean isDisposed() {
    return compositeDisposable.isDisposed();
  }

  public Scheduler getScheduler() {
    return scheduler;
  }
}

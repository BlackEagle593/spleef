package de.eaglefamily.minecraft.spleef.repository;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;
import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Singleton
public class DummySpleefStatsRepository implements SpleefStatsRepository {

  private final Map<UUID, Stats> statsStore = Maps.newConcurrentMap();

  @Override
  public Maybe<Stats> getStats(UUID uniqueId) {
    return Maybe.<Stats>create(emitter -> emitStats(uniqueId, emitter)).observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  @Override
  public Completable saveStats(Stats stats) {
    return Completable.fromAction(() -> statsStore.put(stats.getUniqueId(), stats))
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  private void emitStats(UUID uniqueId, MaybeEmitter<Stats> emitter) {
    Stats stats = statsStore.get(uniqueId);
    if (Objects.nonNull(stats)) {
      emitter.onSuccess(stats);
    }

    emitter.onComplete();
  }
}

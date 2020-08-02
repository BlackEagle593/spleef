package de.eaglefamily.minecraft.spleef.repository;

import com.google.common.collect.Maps;
import de.eaglefamily.minecraft.spleef.repository.model.PlayerName;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DummyPlayerNameRepository implements PlayerNameRepository {

  private final Map<UUID, String> playerNameStore = Maps.newConcurrentMap();

  @Override
  public Maybe<PlayerName> getPlayerName(UUID uniqueId) {
    return Maybe.<PlayerName>create(emitter -> emitName(uniqueId, emitter)).observeOn(
        Schedulers.io()).subscribeOn(Schedulers.computation());
  }

  @Override
  public Completable setPlayerName(UUID uniqueId, String name) {
    return Completable.fromAction(() -> playerNameStore.put(uniqueId, name))
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  private void emitName(UUID uniqueId, MaybeEmitter<PlayerName> emitter) {
    String name = playerNameStore.get(uniqueId);
    if (Objects.nonNull(name)) {
      emitter.onSuccess(PlayerName.create(uniqueId, name));
    }

    emitter.onComplete();
  }
}

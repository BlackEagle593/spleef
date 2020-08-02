package de.eaglefamily.minecraft.spleef.repository;

import de.eaglefamily.minecraft.spleef.repository.model.PlayerName;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

public interface PlayerNameRepository {

  Maybe<PlayerName> getPlayerName(UUID uniqueId);

  Completable setPlayerName(UUID uniqueId, String name);
}

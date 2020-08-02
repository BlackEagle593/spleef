package de.eaglefamily.minecraft.spleef.repository;

import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import java.util.UUID;

public interface SpleefStatsRepository {

  Maybe<Stats> getStats(UUID uniqueId);

  Completable saveStats(Stats stats);
}

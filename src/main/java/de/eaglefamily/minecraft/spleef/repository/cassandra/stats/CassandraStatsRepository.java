package de.eaglefamily.minecraft.spleef.repository.cassandra.stats;

import static com.google.common.base.Preconditions.checkNotNull;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.repository.SpleefStatsRepository;
import de.eaglefamily.minecraft.spleef.repository.cassandra.CassandraConnection;
import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Objects;
import java.util.UUID;

public class CassandraStatsRepository implements SpleefStatsRepository {

  private final CassandraConnection cassandraConnection;
  private final CassandraStatsQuery statsQuery;
  private final CassandraStatsAdapter statsAdapter;

  @Inject
  public CassandraStatsRepository(CassandraConnection cassandraConnection,
      CassandraStatsQuery statsQuery, CassandraStatsAdapter statsAdapter) {
    this.cassandraConnection = cassandraConnection;
    this.statsQuery = statsQuery;
    this.statsAdapter = statsAdapter;

    SimpleStatement createTableStatement = statsQuery.buildCreateStatsTable();
    cassandraConnection.getSession().execute(createTableStatement);
  }

  @Override
  public Maybe<Stats> getStats(UUID uniqueId) {
    checkNotNull(uniqueId);
    return Maybe.<Stats>create(emitter -> emitStats(uniqueId, emitter)).observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  @Override
  public Completable saveStats(Stats stats) {
    checkNotNull(stats);
    return Completable.fromAction(() -> performSaveStats(stats))
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  private void emitStats(UUID uniqueId, MaybeEmitter<Stats> emitter) {
    SimpleStatement statement = statsQuery.buildGetStats(uniqueId);
    ResultSet resultSet = cassandraConnection.getSession().execute(statement);
    Row row = resultSet.one();
    if (Objects.nonNull(row)) {
      Stats stats = statsAdapter.fromRow(row);
      emitter.onSuccess(stats);
    }

    emitter.onComplete();
  }

  private void performSaveStats(Stats stats) {
    SimpleStatement statement = statsQuery.buildSetStats(stats.getUniqueId(), stats.getKills(),
        stats.getDeaths());
    cassandraConnection.getSession().execute(statement);
  }
}

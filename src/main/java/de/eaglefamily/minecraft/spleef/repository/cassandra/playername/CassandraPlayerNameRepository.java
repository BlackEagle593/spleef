package de.eaglefamily.minecraft.spleef.repository.cassandra.playername;

import static com.google.common.base.Preconditions.checkNotNull;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.google.inject.Inject;
import de.eaglefamily.minecraft.spleef.repository.PlayerNameRepository;
import de.eaglefamily.minecraft.spleef.repository.cassandra.CassandraConnection;
import de.eaglefamily.minecraft.spleef.repository.model.PlayerName;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Objects;
import java.util.UUID;

public class CassandraPlayerNameRepository implements PlayerNameRepository {

  private final CassandraConnection cassandraConnection;
  private final CassandraPlayerNameQuery playerNameQuery;
  private final CassandraPlayerNameAdapter playerNameAdapter;

  @Inject
  public CassandraPlayerNameRepository(CassandraConnection cassandraConnection,
      CassandraPlayerNameQuery playerNameQuery, CassandraPlayerNameAdapter playerNameAdapter) {
    this.cassandraConnection = cassandraConnection;
    this.playerNameQuery = playerNameQuery;
    this.playerNameAdapter = playerNameAdapter;

    SimpleStatement createTableStatement = playerNameQuery.buildCreatePlayerNameTable();
    cassandraConnection.getSession().execute(createTableStatement);
  }

  @Override
  public Maybe<PlayerName> getPlayerName(UUID uniqueId) {
    checkNotNull(uniqueId);
    return Maybe.<PlayerName>create(emitter -> emitPlayerName(uniqueId, emitter)).observeOn(
        Schedulers.io()).subscribeOn(Schedulers.computation());
  }

  @Override
  public Completable setPlayerName(UUID uniqueId, String name) {
    checkNotNull(uniqueId);
    checkNotNull(name);
    return Completable.fromAction(() -> performSetPlayerName(uniqueId, name))
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.computation());
  }

  private void emitPlayerName(UUID uniqueId, MaybeEmitter<PlayerName> emitter) {
    SimpleStatement statement = playerNameQuery.buildGetPlayerName(uniqueId);
    ResultSet resultSet = cassandraConnection.getSession().execute(statement);
    Row row = resultSet.one();
    if (Objects.nonNull(row)) {
      PlayerName playerName = playerNameAdapter.fromRow(row);
      emitter.onSuccess(playerName);
    }

    emitter.onComplete();
  }

  private void performSetPlayerName(UUID unqueId, String name) {
    SimpleStatement statement = playerNameQuery.buildSetPlayerName(unqueId, name);
    cassandraConnection.getSession().execute(statement);
  }
}

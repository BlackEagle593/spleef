package de.eaglefamily.minecraft.spleef.repository.cassandra.playername;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.BuildableQuery;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import java.util.UUID;

class CassandraPlayerNameQuery {

  private final BuildableQuery createPlayerNameTableQuery;
  private final BuildableQuery getPlayerNameQuery;
  private final BuildableQuery setPlayerNameQuery;

  CassandraPlayerNameQuery() {
    createPlayerNameTableQuery = prepareCreatePlayerNameTableQuery();
    getPlayerNameQuery = prepareGetPlayerNameQuery();
    setPlayerNameQuery = prepareSetPlayerNameQuery();
  }

  private BuildableQuery prepareCreatePlayerNameTableQuery() {
    return createTable(CassandraPlayerNameSchema.TABLE_PLAYER_NAME).ifNotExists()
        .withPartitionKey(CassandraPlayerNameSchema.KEY_UNIQUE_ID, DataTypes.UUID)
        .withColumn(CassandraPlayerNameSchema.KEY_PLAYER_NAME, DataTypes.TEXT);
  }

  private BuildableQuery prepareGetPlayerNameQuery() {
    return selectFrom(CassandraPlayerNameSchema.TABLE_PLAYER_NAME).all()
        .where(Relation.column(CassandraPlayerNameSchema.KEY_UNIQUE_ID).isEqualTo(bindMarker()));
  }

  private BuildableQuery prepareSetPlayerNameQuery() {
    return insertInto(CassandraPlayerNameSchema.TABLE_PLAYER_NAME).value(
        CassandraPlayerNameSchema.KEY_UNIQUE_ID, bindMarker())
        .value(CassandraPlayerNameSchema.KEY_PLAYER_NAME, bindMarker());
  }

  SimpleStatement buildCreatePlayerNameTable() {
    return createPlayerNameTableQuery.build();
  }

  SimpleStatement buildGetPlayerName(UUID uniqueId) {
    return getPlayerNameQuery.build(uniqueId);
  }

  SimpleStatement buildSetPlayerName(UUID uniqueId, String name) {
    return setPlayerNameQuery.build(uniqueId, name);
  }
}

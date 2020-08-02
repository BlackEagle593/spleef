package de.eaglefamily.minecraft.spleef.repository.cassandra.stats;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.BuildableQuery;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import java.util.UUID;

class CassandraStatsQuery {

  private final BuildableQuery createStatsTableQuery;
  private final BuildableQuery getStatsQuery;
  private final BuildableQuery setStatsQuery;

  CassandraStatsQuery() {
    createStatsTableQuery = prepareCreateStatsTableQuery();
    getStatsQuery = prepareGetStatsQuery();
    setStatsQuery = prepareSetStatsQuery();
  }

  private BuildableQuery prepareCreateStatsTableQuery() {
    return createTable(CassandraStatsSchema.TABLE_STATS).ifNotExists()
        .withPartitionKey(CassandraStatsSchema.KEY_UNIQUE_ID, DataTypes.UUID)
        .withColumn(CassandraStatsSchema.KEY_KILLS, DataTypes.INT)
        .withColumn(CassandraStatsSchema.KEY_DEATHS, DataTypes.INT);
  }

  private BuildableQuery prepareGetStatsQuery() {
    return selectFrom(CassandraStatsSchema.TABLE_STATS).all()
        .where(Relation.column(CassandraStatsSchema.KEY_UNIQUE_ID).isEqualTo(bindMarker()));
  }

  private BuildableQuery prepareSetStatsQuery() {
    return insertInto(CassandraStatsSchema.TABLE_STATS).value(CassandraStatsSchema.KEY_UNIQUE_ID,
        bindMarker())
        .value(CassandraStatsSchema.KEY_KILLS, bindMarker())
        .value(CassandraStatsSchema.KEY_DEATHS, bindMarker());
  }

  SimpleStatement buildCreateStatsTable() {
    return createStatsTableQuery.build();
  }

  SimpleStatement buildGetStats(UUID uniqueId) {
    return getStatsQuery.build(uniqueId);
  }

  SimpleStatement buildSetStats(UUID uniqueId, int kills, int deaths) {
    return setStatsQuery.build(uniqueId, kills, deaths);
  }
}

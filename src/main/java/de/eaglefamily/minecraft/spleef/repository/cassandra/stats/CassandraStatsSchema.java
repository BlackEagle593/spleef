package de.eaglefamily.minecraft.spleef.repository.cassandra.stats;

final class CassandraStatsSchema {

  static final String TABLE_STATS = "stats";
  static final String KEY_UNIQUE_ID = "unique_id";
  static final String KEY_KILLS = "kills";
  static final String KEY_DEATHS = "deaths";

  private CassandraStatsSchema() {
  }
}

package de.eaglefamily.minecraft.spleef.repository.cassandra.stats;

import com.datastax.oss.driver.api.core.cql.Row;
import de.eaglefamily.minecraft.spleef.repository.model.Stats;
import java.util.UUID;

class CassandraStatsAdapter {

  Stats fromRow(Row row) {
    UUID uniqueId = row.getUuid(CassandraStatsSchema.KEY_UNIQUE_ID);
    int kills = row.getInt(CassandraStatsSchema.KEY_KILLS);
    int deaths = row.getInt(CassandraStatsSchema.KEY_DEATHS);
    return Stats.create(uniqueId, kills, deaths);
  }
}

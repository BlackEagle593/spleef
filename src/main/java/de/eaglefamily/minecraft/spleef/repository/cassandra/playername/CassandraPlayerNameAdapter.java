package de.eaglefamily.minecraft.spleef.repository.cassandra.playername;

import com.datastax.oss.driver.api.core.cql.Row;
import de.eaglefamily.minecraft.spleef.repository.model.PlayerName;
import java.util.UUID;

class CassandraPlayerNameAdapter {

  PlayerName fromRow(Row row) {
    UUID uniqueId = row.getUuid(CassandraPlayerNameSchema.KEY_UNIQUE_ID);
    String name = row.getString(CassandraPlayerNameSchema.KEY_PLAYER_NAME);
    return PlayerName.create(uniqueId, name);
  }
}

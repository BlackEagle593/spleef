package de.eaglefamily.minecraft.spleef.repository.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;

public interface CassandraConnection {

  CqlSession getSession();
}

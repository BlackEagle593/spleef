package de.eaglefamily.minecraft.spleef.repository.cassandra;

import com.google.inject.Binder;
import com.google.inject.Module;

public class CassandraModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(CassandraConnection.class).to(SingleCassandraConnection.class);
  }
}

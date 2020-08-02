package de.eaglefamily.minecraft.spleef.repository;

import com.google.inject.Binder;
import com.google.inject.Module;
import de.eaglefamily.minecraft.spleef.repository.cassandra.CassandraModule;
import de.eaglefamily.minecraft.spleef.repository.cassandra.playername.CassandraPlayerNameRepository;
import de.eaglefamily.minecraft.spleef.repository.cassandra.stats.CassandraStatsRepository;

public class RepositoryModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(PlayerNameRepository.class).to(CassandraPlayerNameRepository.class);
    binder.bind(SpleefStatsRepository.class).to(CassandraStatsRepository.class);
    binder.install(new CassandraModule());
  }
}

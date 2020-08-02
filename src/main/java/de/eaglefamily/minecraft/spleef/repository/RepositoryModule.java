package de.eaglefamily.minecraft.spleef.repository;

import com.google.inject.Binder;
import com.google.inject.Module;

public class RepositoryModule implements Module {

  @Override
  public void configure(Binder binder) {
    binder.bind(PlayerNameRepository.class).to(DummyPlayerNameRepository.class);
    binder.bind(SpleefStatsRepository.class).to(DummySpleefStatsRepository.class);
  }
}

package de.eaglefamily.minecraft.spleef.repository.model;

import java.util.UUID;

public class PlayerName {

  private final UUID uniqueId;
  private String name;

  private PlayerName(UUID uniqueId, String name) {
    this.uniqueId = uniqueId;
    this.name = name;
  }

  public static PlayerName create(UUID uniqueId, String name) {
    return new PlayerName(uniqueId, name);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getName() {
    return name;
  }
}

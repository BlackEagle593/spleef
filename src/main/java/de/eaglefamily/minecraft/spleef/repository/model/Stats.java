package de.eaglefamily.minecraft.spleef.repository.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

public class Stats {

  private final UUID uniqueId;
  private int kills;
  private int deaths;

  private Stats(UUID uniqueId, int kills, int deaths) {
    this.uniqueId = uniqueId;
    this.kills = kills;
    this.deaths = deaths;
  }

  /**
   * Create a stats object.
   *
   * @param uniqueId the uniqueId of the player
   * @param kills    the kill count
   * @param deaths   the death count
   * @return the stats object
   */
  public static Stats create(UUID uniqueId, int kills, int deaths) {
    checkNotNull(uniqueId);
    checkArgument(kills >= 0, "Kills cannot be less than zero");
    checkArgument(deaths >= 0, "Deaths cannot be less than zero");
    return new Stats(uniqueId, kills, deaths);
  }

  public static Stats empty(UUID uniqueId) {
    return create(uniqueId, 0, 0);
  }

  /**
   * Create a stats object which combines and sums up the kills and deaths of the stats.
   *
   * @param stats1 the first stats
   * @param stats2 the second stats
   * @return the combined stats object
   */
  public static Stats combined(Stats stats1, Stats stats2) {
    checkNotNull(stats1);
    checkNotNull(stats2);
    checkArgument(stats1.getUniqueId().equals(stats2.getUniqueId()),
        "Combined stats requires the same uniqueId");
    return new Stats(stats1.uniqueId, stats1.kills + stats2.kills, stats1.deaths + stats2.deaths);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public int getKills() {
    return kills;
  }

  public void incrementKills() {
    kills++;
  }

  public int getDeaths() {
    return deaths;
  }

  public void incrementDeaths() {
    deaths++;
  }

  public double getKillDeathRatio() {
    return kills / (double) Math.max(deaths, 1);
  }
}

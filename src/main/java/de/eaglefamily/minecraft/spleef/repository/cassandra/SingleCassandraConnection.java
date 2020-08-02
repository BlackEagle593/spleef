package de.eaglefamily.minecraft.spleef.repository.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.google.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.Objects;

@Singleton
public class SingleCassandraConnection implements CassandraConnection {

  private static final String DEFAULT_HOST = "cassandra";
  private static final int DEFAULT_PORT = 9042;

  private final CqlSession cqlSession;

  /**
   * Create an instance of single cassandra connection.
   */
  public SingleCassandraConnection() {
    ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(CqlSession.class.getClassLoader());

    try {
      cqlSession = buildCqlSession();
      Runtime.getRuntime().addShutdownHook(new Thread(cqlSession::close));
    } finally {
      Thread.currentThread().setContextClassLoader(originalClassLoader);
    }
  }

  private CqlSession buildCqlSession() {
    CqlSessionBuilder builder = CqlSession.builder();
    builder.addContactPoint(new InetSocketAddress(getHost(), getPort()));
    builder.withLocalDatacenter(System.getenv("cassandra.datacenter"));
    builder.withKeyspace(System.getenv("cassandra.keyspace"));

    String username = System.getenv("cassandra.username");
    String password = System.getenv("cassandra.password");
    if (Objects.nonNull(username) && Objects.nonNull(password)) {
      builder.withAuthCredentials(username, password);
    }

    return builder.build();
  }

  private String getHost() {
    String host = System.getenv("cassandra.host");
    return Objects.nonNull(host) ? host : DEFAULT_HOST;
  }

  private int getPort() {
    String port = System.getenv("cassandra.port");
    if (Objects.isNull(port)) {
      return DEFAULT_PORT;
    }

    try {
      return Integer.parseInt(port);
    } catch (NumberFormatException e) {
      throw new NumberFormatException(String.format("Port %s is not valid.", port));
    }
  }

  @Override
  public CqlSession getSession() {
    return cqlSession;
  }
}

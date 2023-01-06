package be.sgerard.kafka;

import org.junit.Test;

public class MySinkConnectorConfigTest {
  @Test
  public void doc() {
    System.out.println(MySinkConnectorConfig.conf().toRst());
  }
}

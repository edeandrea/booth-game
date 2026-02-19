package io.quarkus.game;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.agent.tool.Tool;

@ApplicationScoped
public class Tools {
  @Tool("Gets the current date")
  public LocalDate getCurrentDate() {
    return LocalDate.now();
  }

  @Tool("Gets the system's hostname")
  public String getSystemHostName() throws UnknownHostException {
    return InetAddress.getLocalHost().getHostName();
  }
}

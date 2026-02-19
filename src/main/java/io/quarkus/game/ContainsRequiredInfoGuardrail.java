package io.quarkus.game;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

@ApplicationScoped
public class ContainsRequiredInfoGuardrail implements OutputGuardrail {
  static final String MISSING_DATE_MESSAGE = "Your story doesn't contain the current date. Please try again (and use the tool that was given to you!).";
  static final String MISSING_HOSTNAME_MESSAGE = "Your story doesn't contain the hostname. Please try again (and use the tool that was given to you!).";

  @Override
  public OutputGuardrailResult validate(AiMessage responseFromLLM) {
    var story = responseFromLLM.text();

    if ((story == null) || story.strip().isBlank()) {
      return reprompt("No story", "You didn't write anything. Please try again.");
    }

    if (!story.contains(getHostname())) {
      return reprompt("Missing hostname", MISSING_HOSTNAME_MESSAGE);
    }

    if (!story.contains(getCurrentDate())) {
      return reprompt("Missing date", MISSING_DATE_MESSAGE);
    }

    return success();
  }

  private String getCurrentDate() {
    return LocalDate.now().toString();
  }

  private String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }
}

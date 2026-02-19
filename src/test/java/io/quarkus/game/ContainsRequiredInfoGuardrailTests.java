package io.quarkus.game;

import static dev.langchain4j.test.guardrail.GuardrailAssertions.assertThat;

import java.net.UnknownHostException;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import dev.langchain4j.data.message.AiMessage;

@QuarkusTest
class ContainsRequiredInfoGuardrailTests {
  @Inject
  ContainsRequiredInfoGuardrail guardrail;

  @Inject
  Tools tools;

  @Test
  void noStory() {
    assertThat(this.guardrail.validate(AiMessage.from("")))
        .hasSingleFailureWithMessageAndReprompt("No story", "You didn't write anything. Please try again.");
  }

  @Test
  void successfulValidation() throws UnknownHostException {
    assertThat(this.guardrail.validate(AiMessage.from("This is a story signed by %s on %s".formatted(this.tools.getSystemHostName(), this.tools.getCurrentDate()))))
        .isSuccessful();
  }

  @Test
  void missingDate() throws UnknownHostException {
    assertThat(this.guardrail.validate(AiMessage.from("This is a story signed by %s".formatted(this.tools.getSystemHostName()))))
        .hasSingleFailureWithMessageAndReprompt("Missing date", ContainsRequiredInfoGuardrail.MISSING_DATE_MESSAGE);
  }

  @Test
  void missingHostname() {
    assertThat(this.guardrail.validate(AiMessage.from("This is a story signed on %s".formatted(this.tools.getCurrentDate()))))
        .hasSingleFailureWithMessageAndReprompt("Missing hostname", ContainsRequiredInfoGuardrail.MISSING_HOSTNAME_MESSAGE);
  }
}
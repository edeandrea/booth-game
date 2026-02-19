package io.quarkus.game;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

@ApplicationScoped
public class BooleanOutputGuardrail implements OutputGuardrail {
  @Override
  public OutputGuardrailResult validate(AiMessage responseFromLLM) {
    var text = responseFromLLM.text().strip().toLowerCase();

    return "true".equals(text) || "false".equals(text) ?
        success() :
        reprompt("Invalid response", "Please answer with either 'true' or 'false'.");
  }
}

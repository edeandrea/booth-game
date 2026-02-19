package io.quarkus.game;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.game.Storyteller.Tone;

import io.quarkiverse.langchain4j.RegisterAiService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.OutputGuardrails;

@ApplicationScoped
@RegisterAiService(modelName = "evaluator")
@SystemMessage("""
    You are a story evaluator. Your task is to evaluate the tone of a given story. Reply with 'true' if the tone matches the expected tone, or 'false' otherwise.
    
    ONLY reply with true or false, no other characters
    """)
public interface StorytellerEvaluator {
  @UserMessage("""
      The expected tone is '{tone}'. Here is the story:
      
      {story}
      """)
  @OutputGuardrails(BooleanOutputGuardrail.class)
  boolean isWrittenInTone(String story, Tone tone);
}

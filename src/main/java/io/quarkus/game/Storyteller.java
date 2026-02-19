package io.quarkus.game;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.OutputGuardrails;

@SystemMessage("""
      You are a story teller. You will be given a subject and you will tell a story about it.
      
      You need to "sign" the story with your name and today's date. Your name is the hostname of the current system. You have tools available to obtain the hostname and the current date. Please use them rather than making something up.
      """)
@RegisterAiService(modelName = "storyteller")
@ApplicationScoped
public interface Storyteller {

  enum Tone { SAD, HAPPY, FUNNY, SCARY }

  record StoryRequirements(String subject, Tone tone) {}

  @ToolBox(Tools.class)
  @OutputGuardrails(ContainsRequiredInfoGuardrail.class)
  @UserMessage("Tell me a story about '{requirements.subject}'. It should be written in a '{requirements.tone}' tone.")
  String tellStory(StoryRequirements requirements);
}

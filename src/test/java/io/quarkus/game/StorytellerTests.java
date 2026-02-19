package io.quarkus.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.net.UnknownHostException;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.game.Storyteller.StoryRequirements;
import io.quarkus.game.Storyteller.Tone;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;

import dev.langchain4j.data.message.AiMessage;

@QuarkusTest
class StorytellerTests {
  @Inject
  Storyteller storyTeller;

  @InjectSpy
  Tools tools;

  @InjectSpy
  ContainsRequiredInfoGuardrail guardrail;

  @Inject
  StorytellerEvaluator storyEvaluator;

  @Test
  void storyTellerFollowsRules() throws UnknownHostException {
    var requirements = new StoryRequirements("having to write a story at a boring Java conference", Tone.FUNNY);
    var story = this.storyTeller.tellStory(requirements);

    assertThat(story).isNotBlank();

    verify(this.tools, atLeastOnce()).getCurrentDate();
    verify(this.tools, atLeastOnce()).getSystemHostName();
    verify(this.guardrail, atLeastOnce()).validate(any(AiMessage.class));
    verify(this.guardrail).success();

    assertThat(this.storyEvaluator.isWrittenInTone(story, requirements.tone())).isTrue();
  }
}
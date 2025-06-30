package dev.langchain4j.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;
import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class AiServicesWithStructuredOutputIT {

    @Spy
    ChatModel model = OpenAiChatModel.builder()
            .baseUrl(System.getenv("OPENAI_BASE_URL"))
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .organizationId(System.getenv("OPENAI_ORGANIZATION_ID"))
            .modelName(GPT_4_O_MINI)
            .temperature(0.0)
            .logRequests(true)
            .logResponses(true)
            .build();

    static class Person {

        @JsonProperty(required = true)
        String name;

        Integer age;
    }

    /**
     * NOTE:
     * When used with the "structured outputs" feature, all POJO fields and sub-fields
     * are considered <b>optional</b> by default.
     * This is different from "tools" (see {@link AiServicesWithToolsWithRequiredIT}),
     * where all fields and sub-fields are considered <b>required</b> by default.
     */
    @Test
    void should_extract_pojo_with_required_field() {

        // given
        interface PersonExtractor {
            Person extractPersonFrom(String text);
        }

        PersonExtractor personExtractor = AiServices.create(PersonExtractor.class, model);

        String text = "Klaus is 37 years old";

        // when
        Person person = personExtractor.extractPersonFrom(text);

        // then
        assertThat(person.name).isEqualTo("Klaus");
        assertThat(person.age).isEqualTo(37);
    }

    @Test
    void should_extract_list_of_pojos_with_required_field() {

        // given
        interface PersonsExtractor {
            @UserMessage("""
                Analyze the following text and extract a json representing all people mentioned.
                Return only the json, without any additional text.
                
                "{{text}}"
                """)
            List<Person> extractPersonsFrom(@V("text") String text);
        }

        PersonsExtractor personsExtractor = AiServices.create(PersonsExtractor.class, model);

        String text = "Alice is 30 years old and her brother Bob is 25";

        // when
        List<Person> persons = personsExtractor.extractPersonsFrom(text);

        assertThat(persons.get(0).name).isEqualTo("Alice");
        assertThat(persons.get(0).age).isEqualTo(30);
        assertThat(persons.get(1).name).isEqualTo("Bob");
        assertThat(persons.get(1).age).isEqualTo(25);
    }
}

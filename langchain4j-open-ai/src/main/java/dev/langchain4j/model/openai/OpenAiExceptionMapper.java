package dev.langchain4j.model.openai;

import dev.ai4j.openai4j.OpenAiHttpException;
import dev.langchain4j.model.chat.policy.ExceptionMapper;

class OpenAiExceptionMapper implements ExceptionMapper {

    static final OpenAiExceptionMapper INSTANCE = new OpenAiExceptionMapper();

    @Override
    public RuntimeException mapException(Exception e) {
        if (e instanceof OpenAiHttpException openAiHttpException) {
            return openAiHttpException;
        }
        if (e.getCause() instanceof OpenAiHttpException openAiHttpException) {
            return openAiHttpException;
        }
        return e instanceof RuntimeException re ? re : new RuntimeException(e);
    }
}

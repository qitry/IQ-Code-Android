package com.termux.app.iqcode.api;

import android.content.Context;

import com.termux.app.iqcode.model.SessionConfig;

/** Selects a Java-native wire provider. No CLI subprocess is involved. */
public final class ModelProviders {
    private ModelProviders() {}

    public static ModelProvider forConfig(SessionConfig config) {
        return forConfig(config, null);
    }

    public static ModelProvider forConfig(SessionConfig config, Context context) {
        String protocol = config == null || config.protocol == null ? "anthropic" : config.protocol;
        if ("openai-responses".equals(protocol) || "codex-responses".equals(protocol)) {
            return new OpenAIResponsesProvider();
        }
        if ("openai-compatible".equals(protocol) || "openai-chat".equals(protocol)) {
            return new OpenAIChatCompletionsProvider();
        }
        if ("anthropic".equals(protocol)) return new AnthropicMessagesProvider();
        if ("zcode".equals(protocol)) return new ZcodePlanProvider(context);
        if ("deepseek-free".equals(protocol)) {
            if (context == null) {
                throw new IllegalArgumentException("DeepSeek 免费网页版协议需要应用上下文");
            }
            return new DeepSeekWebProvider(context);
        }
        throw new IllegalArgumentException("Protocol is not Java-ported yet: " + protocol);
    }
}

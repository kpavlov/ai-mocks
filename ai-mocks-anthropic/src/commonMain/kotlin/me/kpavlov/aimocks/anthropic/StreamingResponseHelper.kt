package me.kpavlov.aimocks.anthropic

import com.anthropic.models.ContentBlock
import com.anthropic.models.Message
import com.anthropic.models.RawContentBlockDeltaEvent
import com.anthropic.models.RawContentBlockStartEvent
import com.anthropic.models.RawMessageStartEvent
import com.anthropic.models.RawMessageStopEvent
import com.anthropic.models.TextBlock
import com.anthropic.models.Usage
import io.ktor.sse.ServerSentEvent
import java.util.Optional

internal object StreamingResponseHelper {
    @Suppress("MagicNumber")
    internal fun createMessageStartChunk(
        id: Int,
        model: String,
        serializer: (Any) -> String,
    ): ServerSentEvent {
        val data =
            RawMessageStartEvent
                .builder()
                .message(
                    Message
                        .builder()
                        .id(id.toString())
                        .model(model)
                        .usage(
                            Usage
                                .builder()
                                .inputTokens(25)
                                .outputTokens(1)
                                .cacheCreationInputTokens(Optional.empty())
                                .cacheReadInputTokens(Optional.empty())
                                .build(),
                        ).addContent(
                            ContentBlock.ofText(
                                TextBlock
                                    .builder()
                                    .text("")
                                    .citations(Optional.empty())
                                    .build(),
                            ),
                        ).stopReason(Optional.empty())
                        .stopSequence(Optional.empty())
                        .build(),
                ).build()
        return ServerSentEvent(
            event = data._type().asStringOrThrow(),
            data =
                serializer.invoke(
                    data,
                ),
        )
    }

    internal fun createContentBlockStartChunk(
        index: Long = 0,
        serializer: (Any) -> String,
    ): ServerSentEvent {
        val data =
            RawContentBlockStartEvent
                .builder()
                .index(index)
                .contentBlock(
                    TextBlock
                        .builder()
                        .text("")
                        .citations(Optional.empty())
                        .build(),
                ).build()
        return ServerSentEvent(
            event = data._type().asStringOrThrow(),
            data =
                serializer.invoke(
                    data,
                ),
        )
    }

    @Suppress("LongParameterList")
    internal fun createTextDeltaChunk(
        index: Long = 0,
        content: String,
        serializer: (Any) -> String,
    ): ServerSentEvent {
        val data =
            RawContentBlockDeltaEvent
                .builder()
                .index(index)
                .textDelta(content)
                .build()
        return ServerSentEvent(
            event = data._type().asStringOrThrow(),
            data = serializer.invoke(data),
        )
    }

    internal fun createMessageStopChunk(serializer: (Any) -> String): ServerSentEvent {
        val data =
            RawMessageStopEvent
                .builder()
                .build()
        return ServerSentEvent(
            event = data._type().asStringOrThrow(),
            data =
                serializer.invoke(
                    data,
                ),
        )
    }

    internal fun createPingEvent(): ServerSentEvent =
        ServerSentEvent(
            event = "ping",
            // language=json
            data = """{"type":"ping"}""",
        )

    internal fun randomIdString(
        prefix: String = "",
        length: Int = 24,
    ): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return prefix +
            (1..length)
                .map { chars.random() }
                .joinToString("")
    }
}

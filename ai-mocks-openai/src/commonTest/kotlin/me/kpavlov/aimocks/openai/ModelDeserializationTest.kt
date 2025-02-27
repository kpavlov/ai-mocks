package me.kpavlov.aimocks.openai

import kotlinx.serialization.json.Json
import kotlin.test.Test

internal class ModelDeserializationTest {
    val jsonParser =
        Json {
            ignoreUnknownKeys = false
        }

    @Test
    fun `Should deserialize model`() {
        val json =
            // language=json
            """
            {
              "messages" : [ {
                "content" : "You are a helpful assistant",
                "role" : "system"
              }, {
                "content" : "Help me, please",
                "role" : "user"
              } ],
              "model" : "gpt-4o-mini",
              "response_format" : {
                "type" : "json_object"
              },
              "stream" : false,
              "temperature" : 0.0
            }
            """.trimIndent()

        jsonParser.decodeFromString<ChatCompletionRequest>(json)
    }
}

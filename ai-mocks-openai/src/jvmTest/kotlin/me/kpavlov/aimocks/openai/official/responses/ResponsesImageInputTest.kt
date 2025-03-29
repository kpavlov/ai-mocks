package me.kpavlov.aimocks.openai.official.responses

import com.openai.models.responses.EasyInputMessage
import com.openai.models.responses.EasyInputMessage.Content.Companion.ofResponseInputMessageContentList
import com.openai.models.responses.ResponseCreateParams
import com.openai.models.responses.ResponseInputContent.Companion.ofInputImage
import com.openai.models.responses.ResponseInputContent.Companion.ofInputText
import com.openai.models.responses.ResponseInputImage
import com.openai.models.responses.ResponseInputItem.Companion.ofEasyInputMessage
import com.openai.models.responses.ResponseInputText
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.resource.resourceAsBytes
import io.kotest.matchers.string.shouldContainIgnoringCase
import io.kotest.matchers.string.shouldStartWith
import io.ktor.util.encodeBase64
import me.kpavlov.aimocks.openai.official.AbstractOpenaiTest
import me.kpavlov.aimocks.openai.openai
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTimedValue

@TestInstance(Lifecycle.PER_CLASS)
internal class ResponsesImageInputTest : AbstractOpenaiTest() {
    private lateinit var base64Image: String
    private lateinit var base64ImageUrl: String

    @BeforeAll
    fun beforeAll() {
        base64Image = resourceAsBytes("/pipiro.webp").encodeBase64()
        base64ImageUrl = "data:image/webp;base64,$base64Image"
    }

    @Test
    @Suppress("LongMethod")
    fun `Should describe image input`() {
        modelName = "gpt-4o-mini" // cheap model
        openai.responses {
            temperature = temperatureValue
            model = modelName
            instructionsContains("You are an art expert")
            userMessageContains("what's in this image?")
            containsInputImageWithUrl(base64ImageUrl)
        } respond {
            assistantContent = """
                    The image depicts a cute, cartoonish creature resembling a small,
                    fluffy animal, possibly inspired by a bear or a similar character.
                    """
            delay = 200.milliseconds
        }

        val imageInput =
            ResponseInputImage
                .builder()
                .imageUrl(base64ImageUrl)
                .detail(ResponseInputImage.Detail.AUTO)
                .build()

        val input =
            ResponseCreateParams.Input.ofResponse(
                listOf(
                    ofEasyInputMessage(
                        EasyInputMessage
                            .builder()
                            .role(EasyInputMessage.Role.USER)
                            .content(
                                ofResponseInputMessageContentList(
                                    listOf(
                                        ofInputText(
                                            ResponseInputText
                                                .builder()
                                                .text(
                                                    "what's in this image?",
                                                ).build(),
                                        ),
                                        ofInputImage(imageInput),
                                    ),
                                ),
                            ).build(),
                    ),
                ),
            )
        val params =
            ResponseCreateParams
                .builder()
                .temperature(temperatureValue)
                .maxOutputTokens(maxCompletionTokensValue)
                .model(modelName)
                .instructions("You are an art expert")
                .input(input)
                .build()

        val timedValue =
            measureTimedValue {
                client.responses().create(params)
            }

        val response = timedValue.value

        logger.debug { "Response: $response" }
        val message = response.output().first().asMessage()

        val assistantText =
            message
                .content()
                .first()
                .asOutputText()
                .text()
        logger.info { "Assistant text: $assistantText" }

        assistantText shouldContainIgnoringCase "creature"

        response.model().asString() shouldStartWith modelName
        timedValue.duration shouldBeGreaterThan 200.milliseconds
    }
}

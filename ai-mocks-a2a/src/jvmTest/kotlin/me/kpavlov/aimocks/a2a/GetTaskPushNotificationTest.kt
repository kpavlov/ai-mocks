package me.kpavlov.aimocks.a2a

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import me.kpavlov.aimocks.a2a.model.AuthenticationInfo
import me.kpavlov.aimocks.a2a.model.GetTaskPushNotificationRequest
import me.kpavlov.aimocks.a2a.model.GetTaskPushNotificationResponse
import me.kpavlov.aimocks.a2a.model.PushNotificationConfig
import me.kpavlov.aimocks.a2a.model.TaskId
import me.kpavlov.aimocks.a2a.model.TaskIdParams
import me.kpavlov.aimocks.a2a.model.TaskPushNotificationConfig
import kotlin.test.Test

internal class GetTaskPushNotificationTest : AbstractTest() {
    /**
     * https://github.com/google/A2A/blob/gh-pages/documentation.md#send-a-task
     */
    @Test
    fun `Should get TaskPushNotification config`() =
        runTest {
            val taskId: TaskId = "task_12345"
            val config =
                TaskPushNotificationConfig(
                    id = taskId,
                    pushNotificationConfig =
                        PushNotificationConfig(
                            url = "https://example.com/callback",
                            token = "abc.def.jk",
                            authentication =
                                AuthenticationInfo(
                                    schemes = listOf("Bearer"),
                                ),
                        ),
                )

            a2aServer.getTaskPushNotification() responds {
                id = 1
                result = config
            }

            val response =
                a2aClient
                    .post("/") {
                        val jsonRpcRequest =
                            GetTaskPushNotificationRequest(
                                id = "1",
                                params =
                                    TaskIdParams(
                                        id = taskId,
                                    ),
                            )
                        contentType(ContentType.Application.Json)
                        setBody(Json.encodeToString(jsonRpcRequest))
                    }.call
                    .response

            response.status.shouldBeEqual(HttpStatusCode.OK)
            val body = response.body<String>()
            logger.info { "body = $body" }
            val payload = Json.decodeFromString<GetTaskPushNotificationResponse>(body)
            payload shouldBeEqualToComparingFields
                GetTaskPushNotificationResponse(
                    id = 1,
                    result = config,
                )
        }
}

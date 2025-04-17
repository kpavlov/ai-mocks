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
import me.kpavlov.aimocks.a2a.model.SetTaskPushNotificationRequest
import me.kpavlov.aimocks.a2a.model.SetTaskPushNotificationResponse
import me.kpavlov.aimocks.a2a.model.TaskId
import me.kpavlov.aimocks.a2a.model.TaskPushNotificationConfig
import me.kpavlov.aimocks.a2a.model.create
import kotlin.test.Test

internal class SetTaskPushNotificationTest : AbstractTest() {
    /**
     * https://github.com/google/A2A/blob/gh-pages/documentation.md#send-a-task
     */
    @Test
    fun `Should set TaskPushNotification config`() =
        runTest {
            val taskId: TaskId = "task_12345"
            val config =
                TaskPushNotificationConfig.create {
                    id = taskId
                    pushNotificationConfig {
                        url = "https://example.com/callback"
                        token = "abc.def.jk"
                        authentication {
                            credentials = "secret"
                            schemes += "Bearer"
                        }
                    }
                }

            a2aServer.setTaskPushNotification() responds {
                id = 1
                result {
                    id = taskId
                    pushNotificationConfig {
                        url = "https://example.com/callback"
                        token = "abc.def.jk"
                        authentication {
                            credentials = "secret"
                            schemes += "Bearer"
                        }
                    }
                }
            }

            val response =
                a2aClient
                    .post("/") {
                        val jsonRpcRequest =
                            SetTaskPushNotificationRequest(
                                id = "1",
                                params = config,
                            )
                        contentType(ContentType.Application.Json)
                        setBody(Json.encodeToString(jsonRpcRequest))
                    }.call
                    .response

            response.status.shouldBeEqual(HttpStatusCode.OK)
            val body = response.body<String>()
            logger.info { "body = $body" }
            val payload = Json.decodeFromString<SetTaskPushNotificationResponse>(body)
            payload shouldBeEqualToComparingFields
                SetTaskPushNotificationResponse(
                    id = 1,
                    result = config,
                )
        }
}

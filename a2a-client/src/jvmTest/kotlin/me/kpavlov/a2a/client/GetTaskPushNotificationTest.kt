package me.kpavlov.a2a.client

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import kotlinx.coroutines.test.runTest
import me.kpavlov.aimocks.a2a.model.AuthenticationInfo
import me.kpavlov.aimocks.a2a.model.GetTaskPushNotificationResponse
import me.kpavlov.aimocks.a2a.model.PushNotificationConfig
import me.kpavlov.aimocks.a2a.model.TaskId
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

            val payload = client.getTaskPushNotification(taskId)
            logger.info { "response = $payload" }
            payload shouldBeEqualToComparingFields
                GetTaskPushNotificationResponse(
                    id = 1,
                    result = config,
                )
        }
}

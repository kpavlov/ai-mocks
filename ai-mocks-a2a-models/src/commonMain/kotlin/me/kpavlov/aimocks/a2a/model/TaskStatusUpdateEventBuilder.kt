package me.kpavlov.aimocks.a2a.model

import java.util.function.Consumer

/**
 * DSL builder for [TaskStatusUpdateEvent].
 *
 * Example usage:
 * ```
 * val event = taskStatusUpdateEvent {
 *     id = myTaskId
 *     status = TaskStatus.COMPLETED
 *     final = true
 *     metadata = someMetadata
 * }
 * ```
 */
public class TaskStatusUpdateEventBuilder {
    public var id: TaskId? = null
    public var status: TaskStatus? = null
    public var final: Boolean = false
    public var metadata: Metadata? = null

    public fun id(id: TaskId): TaskStatusUpdateEventBuilder {
        this.id = id
        return this
    }

    public fun isFinal(isFinal: Boolean): TaskStatusUpdateEventBuilder {
        this.final = isFinal
        return this
    }

    public fun status(block: TaskStatusBuilder.() -> Unit): TaskStatusUpdateEventBuilder {
        TaskStatusBuilder().apply(block).build().also { status = it }
        return this
    }

    public fun status(block: Consumer<TaskStatusBuilder>): TaskStatusUpdateEventBuilder {
        val builder = TaskStatusBuilder()
        block.accept(builder)
        builder.build().also { status = it }
        return this
    }

    public fun build(): TaskStatusUpdateEvent =
        TaskStatusUpdateEvent(
            id = requireNotNull(id) { "TaskStatusUpdateEvent.id must be provided" },
            status = requireNotNull(status) { "TaskStatusUpdateEvent.status must be provided" },
            final = final,
            metadata = metadata,
        )
}

/**
 * Top-level DSL function for creating [TaskStatusUpdateEvent].
 */
public inline fun taskStatusUpdateEvent(
    init: TaskStatusUpdateEventBuilder.() -> Unit,
): TaskStatusUpdateEvent = TaskStatusUpdateEventBuilder().apply(init).build()

/**
 * Java-friendly top-level DSL function for creating [TaskStatusUpdateEvent].
 */
public fun taskStatusUpdateEvent(
    init: Consumer<TaskStatusUpdateEventBuilder>,
): TaskStatusUpdateEvent {
    val builder = TaskStatusUpdateEventBuilder()
    init.accept(builder)
    return builder.build()
}

/**
 * DSL extension for [TaskStatusUpdateEvent.Companion].
 */
public fun TaskStatusUpdateEvent.Companion.create(
    init: TaskStatusUpdateEventBuilder.() -> Unit,
): TaskStatusUpdateEvent = TaskStatusUpdateEventBuilder().apply(init).build()

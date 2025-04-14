/*
 * Task.kt
 *
 * This code was generated by json-kotlin-schema-codegen - JSON Schema Code Generator
 * See https://github.com/pwall567/json-kotlin-schema-codegen
 *
 * It is not advisable to modify generated code as any modifications will be lost
 * when the generation process is re-run.
 *
 * This was generated from A2A Schema: https://raw.githubusercontent.com/google/A2A/refs/heads/main/specification/json/a2a.json
 */
package me.kpavlov.aimocks.a2a.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("sessionId")
    val sessionId: String? = null,
    @SerialName("status")
    val status: TaskStatus,
    @SerialName("artifacts")
    val artifacts: List<Artifact>? = null,
    @Contextual
    @SerialName("metadata")
    val metadata: Metadata? = null,
) {
    @Serializable
    public open class Metadata
}

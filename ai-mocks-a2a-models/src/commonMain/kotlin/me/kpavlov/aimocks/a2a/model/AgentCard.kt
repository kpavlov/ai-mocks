/*
 * AgentCard.kt
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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AgentCard(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("url")
    val url: String,
    @SerialName("provider")
    val provider: AgentProvider? = null,
    @SerialName("version")
    val version: String,
    @SerialName("documentationUrl")
    val documentationUrl: String? = null,
    @SerialName("capabilities")
    val capabilities: AgentCapabilities,
    @SerialName("authentication")
    val authentication: AgentAuthentication? = null,
    @SerialName("defaultInputModes")
    val defaultInputModes: List<String> = listOf("text"),
    @SerialName("defaultOutputModes")
    val defaultOutputModes: List<String> = listOf("text"),
    @SerialName("skills")
    val skills: List<AgentSkill>,
)

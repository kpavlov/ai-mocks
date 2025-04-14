/*
 * SendTaskResponse.kt
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

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.kpavlov.aimocks.a2a.model.serializers.RequestIdSerializer

private const val cg_str0 = "2.0"

@Serializable
public data class SendTaskResponse(
    @SerialName("jsonrpc")
    @EncodeDefault
    val jsonrpc: String = "2.0",
    @SerialName("id")
    @Serializable(with = RequestIdSerializer::class)
    var id: RequestId? = null,
    @SerialName("result")
    var result: Task? = null, // todo: val
    @SerialName("error")
    val error: JSONRPCError? = null,
) {
    init {
        require(jsonrpc == cg_str0) { "jsonrpc not constant value $cg_str0 - $jsonrpc" }
    }
}

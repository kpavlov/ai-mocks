/*
 * JSONRPCResponse.kt
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
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.kpavlov.aimocks.a2a.model.serializers.RequestIdSerializer

@Serializable
public data class JSONRPCResponse(
    @Contextual
    @SerialName("jsonrpc")
    @EncodeDefault
    val jsonrpc: String = "2.0",
    @Contextual
    @SerialName("id")
    @Serializable(with = RequestIdSerializer::class)
    val id: RequestId? = null,
    @Contextual
    @SerialName("result")
    val result: Any? = null,
    @Contextual
    @SerialName("error")
    val error: JSONRPCError? = null,
) {
    init {
        require(jsonrpc == cg_str0) { "jsonrpc not constant value $cg_str0 - $jsonrpc" }
    }

    private companion object {
        private const val cg_str0 = "2.0"
    }
}

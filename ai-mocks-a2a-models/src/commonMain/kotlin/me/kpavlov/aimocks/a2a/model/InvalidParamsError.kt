/*
 * InvalidParamsError.kt
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
public data class InvalidParamsError(
    /** Error code */
    @SerialName("code")
    val code: Int = -32602,
    /** A short description of the error */
    @SerialName("message")
    val message: String = "Invalid parameters",
    @Contextual
    @SerialName("data")
    val data: Any? = null,
) {
    init {
        require(code == -32602) { "code not constant value -32602 - $code" }
        require(message == cg_str0) { "message not constant value $cg_str0 - $message" }
    }

    private companion object {
        private const val cg_str0 = "Invalid parameters"
    }
}

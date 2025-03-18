package me.kpavlov.mokksy.request

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import io.ktor.http.Headers

/**
 * Custom matcher to verify that the Ktor [Headers] object contains a header with the specified name and value.
 */
internal fun containsHeader(
    name: String,
    expectedValue: String,
): Matcher<Headers> =
    object : Matcher<Headers> {
        override fun test(value: Headers): MatcherResult {
            val actualValue = value[name]
            return MatcherResult(
                actualValue contentEquals expectedValue,
                {
                    "Headers should contain a header '$name' with value '$expectedValue', but was '$actualValue'."
                },
                {
                    "Headers should NOT contain a header '$name' with value '$expectedValue', but it does."
                },
            )
        }
    }

/**
 * Extension function for easier usage.
 */
public infix fun Headers.shouldHaveHeader(header: Pair<String, String>) {
    this should containsHeader(header.first, header.second)
}

/**
 * Extension function to assert that the headers should not contain a specific header.
 */
public infix fun Headers.shouldNotHaveHeader(header: Pair<String, String>) {
    this shouldNot containsHeader(header.first, header.second)
}

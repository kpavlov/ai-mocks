package me.kpavlov.mokksy

import io.kotest.assertions.failure
import io.kotest.matchers.equalityMatcher
import io.ktor.http.HttpMethod
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Head
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpMethod.Companion.Put
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentSkipListSet

internal expect fun createEmbeddedServer(
    host: String = "0.0.0.0",
    port: Int,
    verbose: Boolean = false,
    module: Application.() -> Unit,
): EmbeddedServer<
    out ApplicationEngine,
    out ApplicationEngine.Configuration,
>

/**
 * Represents an embedded mock server capable of handling various HTTP requests and responses for testing purposes.
 * Provides functionality to configure request specifications for different HTTP methods and manage request matching.
 *
 * @constructor Initializes the server with the specified parameters and starts it.
 * @param port The port number on which the server will run. Defaults to 0 (randomly assigned port).
 * @param verbose A flag indicating whether detailed logs should be printed. Defaults to false.
 * @param wait Determines whether the server startup process should block the current thread. Defaults to false.
 * @param configurer A lambda function for setting custom configurations for the server's application module.
 */
@Suppress("TooManyFunctions")
public open class MokksyServer(
    port: Int = 0,
    host: String = "0.0.0.0",
    verbose: Boolean = false,
    wait: Boolean = false,
    configurer: (Application) -> Unit = {},
) {
    private var resolvedPort: Int

    private val server =
        createEmbeddedServer(
            host = host,
            port = port,
            verbose = verbose,
        ) {
            install(SSE)

            install(DoubleReceive)

            routing {
                route("{...}") {
                    handle {
                        handleRequest(
                            this@handle,
                            this@createEmbeddedServer,
                            stubs,
                            verbose,
                        )
                    }
                }
            }
            configurer(this)
        }

    private val stubs = ConcurrentSkipListSet<Stub<*, *>>()

    init {
        server.start(wait = wait)
        runBlocking {
            resolvedPort =
                server.engine
                    .resolvedConnectors()
                    .first()
                    .port
        }
    }

    /**
     * Retrieves the resolved port on which the server is running.
     *
     * @return The currently configured port number for the server.
     */
    public fun port(): Int = resolvedPort

    /**
     * Creates a `RequestSpecification` with the specified HTTP method and additional configuration
     * defined by the given block, and returns a new `BuildingStep` instance for further customization.
     *
     * * @param name An optional name assigned to the Stub for identification or debugging purposes.
     * @param httpMethod The `HttpMethod` to match for the request specification.
     * @param block A lambda used to configure the `RequestSpecificationBuilder`.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P> method(
        name: String? = null,
        httpMethod: HttpMethod,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> {
        val requestSpec =
            RequestSpecificationBuilder<P?>()
                .apply(block)
                .method(equalityMatcher(httpMethod))
                .build()

        return BuildingStep(
            name = name,
            addStub = this::addStub,
            requestSpecification = requestSpec,
        )
    }

    private fun addStub(stub: Stub<*, *>) {
        val added = stubs.add(stub)
        assert(added) { "Duplicate stub detected: $stub" }
    }

    /**
     * Configures a HTTP GET request specification using the provided block and returns a `BuildingStep`
     * instance for further customization.
     *
     * @param P type of the request payload
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> get(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Get, block)

    /**
     * Configures a HTTP GET request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun get(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.get(null, block)

    /**
     * Configures an HTTP POST request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method utilizes the HTTP POST method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload.
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the POST request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> post(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Post, block)

    /**
     * Configures a HTTP POST request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun post(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.post(null, block)

    /**
     * Configures an HTTP DELETE request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method utilizes the HTTP DELETE method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload.
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the DELETE request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> delete(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Delete, block)

    /**
     * Configures a HTTP DELETE request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun delete(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.delete(null, block)

    /**
     * Configures an HTTP PATCH request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method uses the HTTP PATCH method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload.
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the PATCH request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> patch(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Patch, block)

    /**
     * Configures a HTTP PATCH request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun patch(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.patch(null, block)

    /**
     * Configures an HTTP PUT request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method utilizes the HTTP PUT method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the PUT request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> put(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Put, block)

    /**
     * Configures a HTTP PUT request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun put(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.put(null, block)

    /**
     * Configures an HTTP HEAD request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method utilizes the HTTP HEAD method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the HEAD request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> head(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Head, block)

    /**
     * Configures a HTTP HEAD request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun head(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.head(null, block)

    /**
     * Configures an HTTP OPTIONS request specification using the provided block and returns a `BuildingStep`
     * instance for further customization. This method uses the HTTP OPTIONS method to define the request
     * specification within the provided lambda.
     *
     * @param P type of the request payload
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the OPTIONS request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun <P : Any> options(
        name: String? = null,
        block: RequestSpecificationBuilder<P?>.() -> Unit,
    ): BuildingStep<P?> = method(name, Options, block)

    /**
     * Configures a HTTP HEAD request specification using the provided block and returns a `BuildingStep`
     * for further customization. This method serves as a convenience shortcut.
     *
     * @param block A lambda used to configure the `RequestSpecificationBuilder` for the GET request.
     * @return A `BuildingStep` instance initialized with the generated request specification.
     */
    public fun options(block: RequestSpecificationBuilder<Any?>.() -> Unit): BuildingStep<Any?> =
        this.options(null, block)

    /**
     * Retrieves a list of all request specifications that have not been matched to any incoming requests.
     * A request is considered unmatched if its match count is zero.
     *
     * @return A list of unmatched request specifications.
     */
    public fun findAllUnmatchedRequests(): List<RequestSpecification<*>> =
        stubs
            .filter {
                it.matchCount() == 0
            }.map { it.requestSpecification }
            .toList()

    /**
     * Resets the match counts for all mappings in the server. Each mapping's match count
     * is set to zero, effectively clearing any record of previous matches.
     *
     * This is useful for resetting the state of the server when reinitializing or performing
     * testing scenarios.
     */
    public fun resetMatchCounts() {
        stubs
            .forEach {
                it.resetMatchCount()
            }
    }

    /**
     * Checks for any unmatched requests by retrieving all request specifications that
     * have not been matched to incoming requests and verifies that the list of unmatched
     * requests is empty.
     *
     * This method ensures that all request mappings have been utilized as expected. If
     * there are unmatched requests, this would indicate that some defined mappings were
     * not triggered during the testing or execution process.
     *
     * Utilizes the `findAllUnmatchedRequests` function to identify unmatched requests and
     * asserts that no unmatched requests exist.
     */
    public fun checkForUnmatchedRequests() {
        val unmatchedRequests = findAllUnmatchedRequests()
        if (unmatchedRequests.isNotEmpty()) {
            failure(
                "The following requests were not matched: ${
                    unmatchedRequests.joinToString {
                        it
                            .toLogString()
                    }
                }",
            )
        }
    }

    /**
     * Shuts down the server by stopping its execution.
     *
     * This method halts any ongoing operations of the server,
     * effectively terminating its current state and releasing any occupied resources.
     * It should be invoked to safely stop the server when it is no longer needed.
     */
    public fun shutdown() {
        server.stop()
    }
}

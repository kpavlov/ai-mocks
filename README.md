# Mokksy and AI-Mocks

[![Maven Central](https://img.shields.io/maven-central/v/me.kpavlov.aimocks/ai-mocks-openai)](https://repo1.maven.org/maven2/me/kpavlov/aimocks/ai-mocks-openai/)
[![Kotlin CI](https://github.com/kpavlov/ai-mocks/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/kpavlov/ai-mocks/actions/workflows/gradle.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4887b8978534404dbc62c4894b630501)](https://app.codacy.com/gh/kpavlov/ai-mocks/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/4887b8978534404dbc62c4894b630501)](https://app.codacy.com/gh/kpavlov/ai-mocks/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![codecov](https://codecov.io/github/kpavlov/ai-mocks/graph/badge.svg?token=449G80QY5S)](https://codecov.io/github/kpavlov/ai-mocks)
[![Api Docs](https://img.shields.io/badge/api-docs-blue)](https://kpavlov.github.io/ai-mocks/api/)

_Mokksy_ and _AI-Mocks_ are mock HTTP and LLM (Large Language Model) servers inspired by WireMock, with support for
response streaming and Server-Side Events (SSE). They are designed to build, test, and mock OpenAI API responses for
development purposes.

# Mokksy

![mokksy-mascot-256.png](mokksy/docs/mokksy-mascot-256.png)

**[Mokksy](mokksy/README.md)** is a mock HTTP server built with [Kotlin](https://kotlinlang.org/)
and [Ktor](https://ktor.io/). It addresses the limitations of WireMock by supporting true SSE and streaming responses,
making it particularly useful for integration testing LLM clients.

## Core Features

- Flexibility to control server response directly via ApplicationCall object.
- Built with Kotest Assertions.
- Fluent modern Kotlin DSL API.
- Support for simulating streamed responses and Server-Side Events (SSE) with delays between chunks.
- Support for simulating response delays.

## Example Usages

### Responding with Predefined Responses

```kotlin
// given
val expectedResponse =
  // language=json
  """
    {
        "response": "Pong"
    }
    """.trimIndent()

mokksy.get {
  path = beEqual("/ping")
  containsHeader("Foo", "bar")
} respondsWith {
  body = expectedResponse
}

// when
val result = client.get("/ping") {
  headers.append("Foo", "bar")
}

// then
assertThat(result.status).isEqualTo(HttpStatusCode.OK)
assertThat(result.bodyAsText()).isEqualTo(expectedResponse)
```

### POST Request

```kotlin
// given
val id = Random.nextInt()
val expectedResponse =
  // language=json
  """
    {
        "id": "$id",
        "name": "thing-$id"
    }
    """.trimIndent()

mokksy.post {
  path = beEqual("/things")
  bodyContains("\"$id\"")
} respondsWith {
  body = expectedResponse
  httpStatus = HttpStatusCode.Created
  headers {
    // type-safe builder style
    append(HttpHeaders.Location, "/things/$id")
  }
  headers += "Foo" to "bar" // list style
}

// when
val result =
  client.post("/things") {
    headers.append("Content-Type", "application/json")
    setBody(
      // language=json
      """
            {
                "id": "$id"
            }
            """.trimIndent(),
    )
  }

// then
assertThat(result.status).isEqualTo(HttpStatusCode.Created)
assertThat(result.bodyAsText()).isEqualTo(expectedResponse)
assertThat(result.headers["Location"]).isEqualTo("/things/$id")
assertThat(result.headers["Foo"]).isEqualTo("bar")
```
### Server-Side Events (SSE) Response

Server-Side Events (SSE) is a technology that allows a server to push updates to the client over a single, long-lived
HTTP connection, enabling real-time updates without requiring the client to continuously poll the server for new data.

```kotlin
mokksy.post {
  path = beEqual("/sse")
} respondsWithSseStream {
  flow =
    flow {
      delay(200.milliseconds)
      emit(
        ServerSentEvent(
          data = "One",
        ),
      )
      delay(50.milliseconds)
      emit(
        ServerSentEvent(
          data = "Two",
        ),
      )
    }
}

// when
val result = client.post("/sse")

// then
assertThat(result.status)
  .isEqualTo(HttpStatusCode.OK)
assertThat(result.contentType())
  .isEqualTo(ContentType.Text.EventStream.withCharsetIfNeeded(Charsets.UTF_8))
assertThat(result.bodyAsText())
  .isEqualTo("data: One\r\ndata: Two\r\n")
```

# AI-Mocks

**AI-Mocks** is a specialized mock server implementations (e.g., mocking OpenAI API) built using Mokksy.

It supports mocking following LLMs:
1. [OpenAI](https://platform.openai.com/docs/api-reference/) - [ai-mocks-openai](ai-mocks-openai)
2. [Anthropic](https://docs.anthropic.com/en/api) - [ai-mocks-anthropic](ai-mocks-anthropic)

**_NB! Not all API endpoints and parameters are supported!_**

## Mocking OpenAI API

See [ai-mocks-openai/README.md](ai-mocks-openai/README.md) for detailed examples and usage instructions.

## Mocking Anthropic API
See [ai-mocks-anthropic/README.md](ai-mocks-anthropic/README.md) for detailed examples and usage instructions.

## How to build

Building project locally:

```shell
gradle build
```

or using Make:

```shell
make
```

## Contributing

I do welcome contributions! Please see the [Contributing Guidelines](CONTRIBUTING.md) for details.

## Enjoying LLM integration testing? :heart:

[![Buy me a Coffee](https://cdn.buymeacoffee.com/buttons/default-orange.png)](https://buymeacoffee.com/mailsk)

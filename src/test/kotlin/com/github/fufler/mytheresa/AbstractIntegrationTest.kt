package com.github.fufler.mytheresa

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriComponentsBuilder
import kotlin.reflect.KClass

abstract class AbstractIntegrationTest {
    private val restTemplate = RestTemplate().apply {
        val baseUrl = System.getProperty("it.baseUrl") ?: DEFAULT_ENDPOINT

        uriTemplateHandler = DefaultUriBuilderFactory(baseUrl)
    }

    protected fun performGetRequest(path: String, params: Map<String, Any> = emptyMap()) =
        performGetRequest(path, params, JsonNode::class)

    protected fun <T : Any> performGetRequest(
        path: String,
        params: Map<String, Any> = emptyMap(),
        responseType: KClass<T>
    ): T {
        val uriBuilder = UriComponentsBuilder.fromPath(path)

        val uri = params
            .entries
            .fold(uriBuilder) { b, entry -> b.queryParam(entry.key, entry.value) }
            .encode()
            .toUriString()

        return restTemplate.getForObject(uri, responseType.java, params)
    }

    init {
        waitForBackend()
    }

    private fun waitForBackend() {
        for (i in 1..CONNECT_ATTEMPTS) {
            try {
                val response = performGetRequest("/actuator/health")

                if (response.get("status")?.textValue() == "UP")
                    return
            } catch (e: Exception) {
                Thread.sleep(CONNECT_INTERVAL)
            }
        }

        throw IllegalStateException("Cannot connect to backend")
    }

    companion object {
        private const val CONNECT_ATTEMPTS = 10
        private const val CONNECT_INTERVAL = 1000L
        private const val DEFAULT_ENDPOINT = "http://localhost:8080"
    }
}
package org.example.helpers.httpHelpers

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class HttpClientHelper {
    private val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(15))
        .build()

    fun getHttpClient(): HttpClient {
        return client
    }

    fun getHttpRequest(url: String): HttpRequest {
        return HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(10))
            .build()
    }

    fun getHttpResponse(url: String): HttpResponse<Void> {
        return client.send(getHttpRequest(url), HttpResponse.BodyHandlers.discarding())
    }
}
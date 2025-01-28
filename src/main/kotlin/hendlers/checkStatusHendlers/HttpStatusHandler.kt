package org.example.helpers.core

import org.example.helpers.httpHelpers.HttpClientHelper
import java.io.BufferedWriter
import java.net.http.HttpResponse

class HttpStatusHandler {

    fun handleSuccessStatus(statusCode: Int, url: String, writer: BufferedWriter) {
        if (statusCode in 200..299) {
            println("Успех (Код состояния: $statusCode): $url")
            writer.write("$url\n")
        }
    }

    fun handleRedirectStatus(statusCode: Int, url: String, writer: BufferedWriter, httpClientHelper: HttpClientHelper) {
        if (statusCode in 300..307) {
            val newUrl = httpClientHelper.getHttpResponse(url).headers().firstValue("Location").orElse(null)
            if (newUrl != null) {
                println("Забираем $newUrl из Location в хедере")
                val responseForHeader = httpClientHelper.getHttpClient()
                    .send(
                        httpClientHelper.getHttpRequest(newUrl),
                        HttpResponse.BodyHandlers.discarding()
                    )
                handleSuccessStatus(responseForHeader.statusCode(), newUrl, writer)
            } else {
                println("Ошибка: заголовок Location отсутствует для $url")
            }
        }
    }

    fun handleErrorStatus(statusCode: Int, url: String) {
        if (statusCode !in 200..307) {
            println("Ошибка (Код состояния: $statusCode): $url")
        }
    }
}
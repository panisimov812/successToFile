package org.example.helpers.core

import org.example.helpers.httpHelpers.HttpClientHelper
import java.io.File
import java.io.IOException
import java.net.http.HttpResponse

class CheckUrlsCore {
    private val httpClientHelper = HttpClientHelper()
    private val httpStatusHandler = HttpStatusHandler()

    fun checkUrlsWithSelenium(inputFile: String, outputFile: String) {
        try {
            if (!File(inputFile).exists()) {
                println("Ошибка: входной файл $inputFile не существует")
                return
            }

            val outputFileHandle = File(outputFile)
            outputFileHandle.bufferedWriter().use { writer ->
                File(inputFile).forEachLine { url ->
                    try {
                        if (!url.startsWith("http")) {
                            println("Некорректный URL: $url")
                            return@forEachLine
                        }

                        val response = httpClientHelper.getHttpClient()
                            .send(
                                httpClientHelper.getHttpRequest(url),
                                HttpResponse.BodyHandlers.discarding()
                            )

                        // Обработка статусов через HttpStatusHandler
                        httpStatusHandler.handleSuccessStatus(response.statusCode(), url, writer)
                        httpStatusHandler.handleRedirectStatus(response.statusCode(), url, writer, httpClientHelper)
                        httpStatusHandler.handleErrorStatus(response.statusCode(), url)
                    } catch (e: IllegalArgumentException) {
                        println("Ошибка формата URL $url: ${e.message}")
                    } catch (e: IOException) {
                        println("Сетевая ошибка при обработке $url: ${e.message}")
                    } catch (e: InterruptedException) {
                        println("Запрос был прерван для $url: ${e.message}")
                        Thread.currentThread().interrupt()
                    }
                }
            }
        } catch (e: IOException) {
            println("Ошибка записи в файл $outputFile: ${e.message}")
        }
    }
}
package org.example


import java.io.File
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

fun checkUrlsWithSelenium(inputFile: String, outputFile: String) {
    val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(15)) // Установить таймаут соединения
        .build()

    try {
        val outputFileHandle = File(outputFile)
        outputFileHandle.bufferedWriter().use { writer ->
            File(inputFile).forEachLine { url ->
                try {
                    // Проверка валидности URL
                    if (!url.startsWith("http")) {
                        println("Некорректный URL: $url")
                        return@forEachLine
                    }

                    val request = HttpRequest.newBuilder()
                        .uri(URI.create(url.trim()))
                        .timeout(Duration.ofSeconds(10))
                        .build()

                    val response = client.send(request, HttpResponse.BodyHandlers.discarding())

                    if (response.statusCode() in 200..299) {
                        println("Успех (Код состояния: ${response.statusCode()}): $url")
                        writer.write("$url\n")
                    } else if (response.statusCode() in 300..302) {
                        val newUrl = response.headers().firstValue("Location").orElse(null)
                        if (newUrl != null) {
                            println("Забираем $newUrl из Location в хедере")
                            val request1 = HttpRequest.newBuilder()
                                .uri(URI.create(newUrl))
                                .timeout(Duration.ofSeconds(10))
                                .build()
                            val response1 = client.send(request1, HttpResponse.BodyHandlers.discarding())
                            if (response1.statusCode() in 200..299) {
                                println("Успех (Код состояния: ${response1.statusCode()}): $newUrl")
                                writer.write("$newUrl\n")
                            } else {
                                println("Ошибка (Код состояния: ${response1.statusCode()}): $newUrl")
                            }
                        } else {
                            println("Ошибка: заголовок Location отсутствует для $url")
                        }
                    } else {
                        println("Ошибка (Код состояния: ${response.statusCode()}): $url")
                    }
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

package org.example

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File
import java.io.IOException

fun checkUrlsWithSelenium(inputFile: String, outputFile: String) {
    val options = ChromeOptions()
    options.addArguments("--headless", "--disable-gpu")

    // Инициализация драйвера
    val driver = ChromeDriver(options)

    try {
        val outputFileHandle = File(outputFile)
        // Открытие файла для записи
        outputFileHandle.bufferedWriter().use { writer ->
            File(inputFile).forEachLine { url ->
                try {
                    driver.get(url.trim())
                    val responseCode = (driver as RemoteWebDriver).executeScript("return document.readyState") as String

                    if (responseCode == "complete") {
                        println("Successful: $url $responseCode")
                        writer.write("$url\n")
                    } else {
                        println("Failed: $url $responseCode")
                    }
                } catch (e: Exception) {
                    println("Ошибка процесса $url: ${e.message}")
                    e.printStackTrace()  // Печать стека ошибки для отладки
                }
            }
        }
    } catch (e: IOException) {
        println("Ошибка записи в файл $outputFile: ${e.message}")
        e.printStackTrace()  // Печать стека ошибки для отладки
    } finally {
        driver.quit()  // Закрытие драйвера в блоке finally для гарантированного закрытия
    }
}

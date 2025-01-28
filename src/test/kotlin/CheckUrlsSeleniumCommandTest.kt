import org.example.checkUrlsWithSelenium
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class CheckUrlsSeleniumCommandTest {

    @Test
    fun testFileIsFilledWithUrls() {
        val inputFile = File.createTempFile("input", ".txt")
        val outputFile = File.createTempFile("output", ".txt")

        inputFile.writeText("https://www.google.com\nhttps://www.example.com\nhttps://nonexistentwebsite.com")

        checkUrlsWithSelenium(inputFile.absolutePath, outputFile.absolutePath)

        assertTrue(outputFile.exists())
        assertTrue(outputFile.readText().contains("https://www.google.com"))
        assertTrue(outputFile.readText().contains("https://www.example.com"))
        assertFalse(outputFile.readText().contains("https://nonexistentwebsite.com"))

        inputFile.delete()
        outputFile.delete()
    }

    @Test
    fun testFileIsFilledWithSuccessfulUrls() {
        val inputFile = File.createTempFile("input", ".txt")
        val outputFile = File.createTempFile("output", ".txt")

        inputFile.writeText("https://www.google.com\nhttps://www.ya.ru\nhttps://yhsdfsdf.ru\nhttps://nonexistentwebsite.com/")

        checkUrlsWithSelenium(inputFile.absolutePath, outputFile.absolutePath)

        assertTrue(outputFile.readText().contains("https://www.google.com"))
        assertTrue(outputFile.readText().contains("https://www.ya.ru"))
        assertTrue(outputFile.readText().contains("https://yhsdfsdf.ru").not())
        assertTrue(outputFile.readText().contains("https://nonexistentwebsite.com").not())

        inputFile.delete()
        outputFile.delete()
    }
}
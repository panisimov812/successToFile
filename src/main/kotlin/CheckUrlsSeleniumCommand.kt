package org.example
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*

class CheckUrlsSeleniumCommand : CliktCommand() {
    private val inputFile: String by option("--input", help = "Path to input file").required()
    private val outputFile: String by option("--output", help = "Path to output file").required()

    override fun run() {
        checkUrlsWithSelenium(inputFile, outputFile)
    }
}

fun main(args: Array<String>) = CheckUrlsSeleniumCommand().main(args)
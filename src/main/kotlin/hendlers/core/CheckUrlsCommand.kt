package org.example.helpers.core

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

class CheckUrlsCommand : CliktCommand() {
    private val inputFile: String by option("--input", help = "Path to input file").required()
    private val outputFile: String by option("--output", help = "Path to output file").required()

    override fun run() {
        val core = CheckUrlsCore()
        core.checkUrlsWithSelenium(inputFile, outputFile)
    }
}

fun main(args: Array<String>) = CheckUrlsCommand().main(args)
package com.example.mvnparser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.toPath


class SandboxTest {

    @Test
    fun parseTest() {
        val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) }).registerKotlinModule().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    @Test
    fun findDuplicates() {
        val oldDependenciesPath = "/allDependencies.txt"
        val newDependenciesPath = "/deps.txt"

        val intersectDepsPath = "intersectDeps.txt"


        val oldDeps = Files.readAllLines(javaClass.getResource(oldDependenciesPath).toURI().toPath()).toSet()
        val newDeps = Files.readAllLines(javaClass.getResource(newDependenciesPath).toURI().toPath()).toSet()

        val intersect = oldDeps.intersect(newDeps)

        intersect.forEach {
            Files.writeString(
                Path(intersectDepsPath),
                it + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND
            )
        }

    }
}

package com.example.mvnparser.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.toPath

class XmlParserImplTest {

    private val xmlParserImpl = XmlParserImpl()

    @Test
    fun `Should parse pom string to pom object`() {
        val pomString = getPomString(POM_PATH)
        val pomObject = xmlParserImpl.parse(pomString!!)

        assertThat(pomObject?.dependencies).isNotNull.allMatch {
            it.groupId != null && it.artifactId != null
        }
        println(pomObject)
    }

    @Test
    fun `Should parse parent pom string to pom object`() {
        val pomString = getPomString(PARENT_POM_PATH)
        val pomObject = xmlParserImpl.parse(pomString!!)

        assertThat(pomObject?.dependencyManagement?.dependencies).isNotNull.allMatch {
            it.groupId != null && it.artifactId != null
        }
        println(pomObject)
    }

    private fun getPomString(path: String): String? = Files.readString(javaClass.getResource(path).toURI().toPath())

    companion object {
        private const val POM_PATH = "/rest-assured-5.3.0.pom.xml"
        private const val PARENT_POM_PATH = "/rest-assured-parent-5.3.0.pom.xml"
    }
}
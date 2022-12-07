package com.example.mvnparser.model.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "project")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Pom(

    @JacksonXmlProperty(localName = "parent")
    @JsonIgnoreProperties(ignoreUnknown = true)
    val parent: Parent?,

    @JacksonXmlElementWrapper(localName = "properties")
    val properties: Map<String, String>?,

    val dependencyManagement: DependencyManagement?,

    @JacksonXmlElementWrapper(localName = "dependencies")
    @JsonIgnoreProperties(ignoreUnknown = true)
    val dependencies: List<Dependency>?
) {
    data class DependencyManagement(
        @JacksonXmlElementWrapper(localName = "dependencies")
        @JsonIgnoreProperties(ignoreUnknown = true)
        val dependencies: List<Dependency>?
    )

    data class Parent(
        val groupId: String,
        val artifactId: String,
        val version: String,
    )

    data class Dependency(
        val groupId: String?,
        val artifactId: String?,
        var version: String?,
    )
}

package com.example.mvnparser

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "project")
data class Pom(

    @JacksonXmlProperty(localName = "parent")
    val parent: Parent,

    @JacksonXmlElementWrapper(localName = "properties")
    val properties: Map<String, String>
) {
    data class Parent(
        val groupId: String,
        val artifactId: String,
        val version: String,
    )
}

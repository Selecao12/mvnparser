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
    ) {
        fun hasUndefinedDependenciesVersion() = dependencies.orEmpty().any {
            it.hasUndefinedVersion()
        }

        fun fillDependenciesVersion(properties: Map<String, String>) {
            dependencies?.forEach {
                properties[it.version]?.let { version ->
                    it.version = version
                }
            }
        }
    }

    data class Parent(
        val groupId: String,
        val artifactId: String,
        val version: String,
    )

    data class Dependency(
        val groupId: String?,
        val artifactId: String?,
        var version: String?,
    ) {
        fun hasUndefinedVersion() = version == null || version!!.startsWith("$\\{")
    }

    fun hasParent() = parent != null
    fun hasProperties() = !properties.isNullOrEmpty()

    fun hasDependencyManagement() = !dependencyManagement?.dependencies.isNullOrEmpty()
    fun hasUndefinedDependenciesVersion() = dependencies.orEmpty().any {
        it.hasUndefinedVersion()
    }

    fun fillDependenciesVersion(properties: Map<String, String>) {
        dependencies?.forEach {
            if (it.version != null && it.version!!.startsWith("\${")) {
                val versionPropsKey = it.version!!.substring(2, it.version!!.length - 1)
                properties[versionPropsKey]?.let { version ->
                    it.version = version
                }
            }
        }
    }

    fun fillDependenciesVersion(dependencyManagement: DependencyManagement) {
        dependencies?.forEach { dependency ->
            dependencyManagement.dependencies.orEmpty().find {
                it.groupId == dependency.groupId && it.artifactId == dependency.artifactId
            }?.let {
                if (dependency.hasUndefinedVersion()) {
                    dependency.version = it.version
                }
            }
        }
    }


}

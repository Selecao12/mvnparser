package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.PomDependenciesVersionResolver
import org.springframework.stereotype.Service


@Service
class PomDependenciesVersionResolverImpl : PomDependenciesVersionResolver {
    override fun resolve(poms: MutableList<Pom>): List<Pom.Dependency> {
        if (poms.isEmpty()) return emptyList()

        setParents(poms)

        fillProjectGroupIdAndVersion(poms)

        val requiredPom = poms.first()

        val properties = getAllProperties(poms)

        val allDependencies = getAllDependencies(requiredPom, poms)

        allDependencies.forEach {
            if (it.version!!.startsWith("\${")) {
                val versionPropsKey = it.version!!.substring(2, it.version!!.length - 1)
                properties[versionPropsKey]?.let { version ->
                    it.version = version
                }
            }
        }

        return allDependencies
    }

    private fun setParents(poms: MutableList<Pom>) {
        for (i in 0 until poms.size) {
            val pom = poms[i]
            if (i < poms.size - 1) {
                pom.parentPom = poms[i + 1]
            }
        }
    }

    private fun fillProjectGroupIdAndVersion(poms: MutableList<Pom>) {
        poms.forEach { pom ->
            pom.properties.orEmpty().forEach {
                if (it.value == "\${project.groupId}") {
                    kotlin.runCatching {
                        pom.properties!![it.key] = pom.getGroupIdOrParentGroupId()!!
                    }.onFailure {
                        println("Something goes wrong ${pom.properties}, ${pom.getGroupIdOrParentGroupId()}") // todo logger
                    }
                }
                if (it.value == "\${project.version}") {
                    kotlin.runCatching {
                        pom.properties!![it.key] = pom.getVersionOrParentVersion()!!
                    }.onFailure {
                        println("Something goes wrong ${pom.properties}, ${pom.getVersionOrParentVersion()}") // todo logger
                    }
                }
            }
            (pom.dependencyManagement?.dependencies.orEmpty() + pom.dependencies.orEmpty()).forEach { dependency ->
                if (dependency.groupId == "\${project.groupId}") {
                    dependency.groupId = pom.getGroupIdOrParentGroupId()
                }
                if (dependency.version == "\${project.version}") {
                    dependency.version = pom.getVersionOrParentVersion()
                }
            }
        }
    }

    private fun getAllProperties(poms: List<Pom>): MutableMap<String, String> {
        return poms.map { it.properties.orEmpty() }
            .fold(mutableMapOf()) { acc, props ->
                acc.also { it.putAll(props) }
            }
    }

    private fun getAllDependencies(requiredPom: Pom, poms: MutableList<Pom>): List<Pom.Dependency> {
        val dependenciesWithVersionOrPropsVersion = requiredPom.dependencies.orEmpty().filter { it.version != null }
        val dependenciesFromManagements = poms.filter { it.dependencyManagement != null }
            .map { it.dependencyManagement!! }
            .flatMap { it.dependencies.orEmpty() }

        return dependenciesWithVersionOrPropsVersion + dependenciesFromManagements
    }
}
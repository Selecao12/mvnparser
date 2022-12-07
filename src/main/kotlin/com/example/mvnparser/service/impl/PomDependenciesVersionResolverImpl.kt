package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.PomDependenciesVersionResolver
import org.springframework.stereotype.Service

@Service
class PomDependenciesVersionResolverImpl : PomDependenciesVersionResolver {
    override fun resolve(poms: MutableList<Pom>): List<Pom.Dependency> {
        if (poms.isEmpty()) return emptyList()

        val requiredPom = poms.first()

        val properties = getAllProperties(poms)

        val dependenciesWithVersionOrPropsVersion = requiredPom.dependencies.orEmpty().filter { it.version != null }

        val dependenciesFromManagements = getDependenciesFromDepsManagements(poms)
        val allDependencies = dependenciesWithVersionOrPropsVersion + dependenciesFromManagements

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

    private fun getDependenciesFromDepsManagements(poms: List<Pom>): List<Pom.Dependency> {
        return poms.filter { it.dependencyManagement != null }
            .map { it.dependencyManagement!! }
            .flatMap { it.dependencies.orEmpty() }
    }

    private fun getAllProperties(poms: List<Pom>): MutableMap<String, String> {
        return poms.map { it.properties.orEmpty() }
            .fold(mutableMapOf()) { acc, props ->
                acc.also { it.putAll(props) }
            }
    }
}
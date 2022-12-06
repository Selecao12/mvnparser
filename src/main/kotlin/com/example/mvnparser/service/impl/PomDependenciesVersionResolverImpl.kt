package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.PomDependenciesVersionResolver
import org.springframework.stereotype.Service

@Service
class PomDependenciesVersionResolverImpl : PomDependenciesVersionResolver {
    override fun resolve(poms: MutableList<Pom>): Pom {
        if (poms.isEmpty()) throw IllegalStateException("No poms provided.")
        if (poms.size == 1) return fillSinglePomDependencies(poms.first()) // fill dependencies

        val requiredPom = poms.first()

        val dependencyManagements = getDependencyManagements(poms)
        val propertiesList = getPropertiesList(poms)

        dependencyManagements.forEach {
            requiredPom.fillDependenciesVersion(it)
        }
        propertiesList.forEach {
            requiredPom.fillDependenciesVersion(it)
        }

        return requiredPom
    }

    private fun getDependencyManagements(poms: List<Pom>): List<Pom.DependencyManagement> {
        return poms.filter { it.hasDependencyManagement() }.map { it.dependencyManagement!! }
    }

    private fun getPropertiesList(poms: List<Pom>): List<Map<String, String>> {
        return poms.filter { it.hasProperties() }.map { it.properties!! }
    }

    private fun fillSinglePomDependencies(pom: Pom): Pom {
        if (pom.hasDependencyManagement()) {
            pom.fillDependenciesVersion(pom.dependencyManagement!!)
        }
        if (pom.hasProperties()) {
            pom.fillDependenciesVersion(pom.properties!!)
        }

        return pom
    }
}
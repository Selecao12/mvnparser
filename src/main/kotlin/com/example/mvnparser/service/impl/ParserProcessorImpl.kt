package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.FeignClient
import com.example.mvnparser.service.ParserProcessor
import com.example.mvnparser.service.PomDependenciesVersionResolver
import com.example.mvnparser.service.XmlParser
import org.springframework.stereotype.Service

@Service
class ParserProcessorImpl(
    private val feignClient: FeignClient,
    private val xmlParser: XmlParser,
    private val pomDependenciesVersionResolver: PomDependenciesVersionResolver,
) : ParserProcessor {
    override fun processAll(implementations: List<String>): Set<String> = implementations.map {
        val (groupId, artifactId, version) = parseImplementation(it)
        process(groupId, artifactId, version)
    }.flatten().toSet()

    private fun parseImplementation(implementation: String): Triple<String, String, String> {
        val split = implementation.split(":")
        if (split.size != 3) throw IllegalStateException("Invalid implementation string.") // todo to validation constraint

        return Triple(split[0], split[1], split[2])
    }

    override fun process(groupId: String, artifactId: String, version: String): List<String> {
        val pomList: MutableList<Pom> = getHierarchicalPomList(groupId, artifactId, version)
        val dependencies = pomDependenciesVersionResolver.resolve(pomList)

        logUnresolved(dependencies, groupId, artifactId, version)

        return getImplementations(dependencies)
    }

    private fun getHierarchicalPomList(groupId: String, artifactId: String, version: String): MutableList<Pom> {
        var pom = requestPom(groupId, artifactId, version)
        val pomList: MutableList<Pom> = mutableListOf(pom)
        while (pom.parent != null) {
            val (parentGroupId, parentArtifactId, parentVersion) = pom.parent!!
            pom = requestPom(parentGroupId, parentArtifactId, parentVersion)
            pomList.add(pom)
        }
        return pomList
    }

    private fun logUnresolved(
        resolvedDependencies: List<Pom.Dependency>,
        groupId: String,
        artifactId: String,
        version: String
    ) {
        val unresolvedDependencies = resolvedDependencies.filter {
            it.groupId == null || it.groupId!!.startsWith("\${") ||
                    it.version == null || it.version!!.startsWith("\${")
        }
        if (unresolvedDependencies.isNotEmpty()) {
            val implementations = buildString {
                unresolvedDependencies.forEach {
                    append(toImplementation(it))
                    append(System.lineSeparator())
                }
            }
            // todo logger
            println(
                "implementation '$groupId:$artifactId:$version' has ${unresolvedDependencies.size} " +
                        "unresolved dependency: ${System.lineSeparator()}$implementations"
            )
        }
    }

    private fun getImplementations(dependencies: List<Pom.Dependency>) =
        dependencies.map { toImplementation(it) }

    private fun toImplementation(dependency: Pom.Dependency): String = buildString {
        append(dependency.groupId)
        append(":")
        append(dependency.artifactId)
        append(":")
        append(dependency.version)
    }

    private fun requestPom(groupId: String, artifactId: String, version: String): Pom {
        val pomString = feignClient.getPom(buildFilepath(groupId, artifactId, version))
        return xmlParser.parse(pomString)
    }

    private fun buildFilepath(groupId: String, artifactId: String, version: String): String {
        val group = groupId.replace('.', '/')
        val artifact = artifactId.replace('.', '/')
        return "$group/$artifact/$version/$artifact-$version.pom"
    }
}
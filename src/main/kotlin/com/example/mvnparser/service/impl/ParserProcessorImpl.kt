package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.FeignClient
import com.example.mvnparser.service.ParserProcessor
import com.example.mvnparser.service.PomDependenciesVersionResolver
import com.example.mvnparser.service.XmlParser
import org.springframework.stereotype.Service

@Service
class ParserProcessorImpl(
    private val feignClient: FeignClient, // todo перенести в RequestServiceImpl, чтобы там маппить строку в data class?
    private val xmlParser: XmlParser,
    private val pomDependenciesVersionResolver: PomDependenciesVersionResolver,
) : ParserProcessor {
    override fun process(groupId: String, artifactId: String, version: String): List<String> {
        val pomList: MutableList<Pom> = getHierarchicalPomList(groupId, artifactId, version)

        val resolvedPom = pomDependenciesVersionResolver.resolve(pomList)

        return getImplementations(resolvedPom)
    }

    override fun processAll(implementations: List<String>): Set<String> = implementations.map {
        val (groupId, artifactId, version) = parseImplementation(it)
        process(groupId, artifactId, version)
    }.flatten().toSet()

    private fun parseImplementation(implementation: String): Triple<String, String, String> {
        val split = implementation.split(":")
        if (split.size != 3) throw IllegalStateException("Invalid implementation string.") // todo to validation constraint

        return Triple(split[0], split[1], split[2])
    }

    private fun getHierarchicalPomList(groupId: String, artifactId: String, version: String): MutableList<Pom> {
        var pom = requestPom(groupId, artifactId, version)
        val pomList: MutableList<Pom> = mutableListOf(pom)
        while (pom.hasParent()) {
            val (parentGroupId, parentArtifactId, parentVersion) = pom.parent!!
            pom = requestPom(parentGroupId, parentArtifactId, parentVersion)
            pomList.add(pom)
        }
        return pomList
    }

    private fun getImplementations(resolvedPom: Pom) =
        resolvedPom.dependencies.orEmpty().map {
            buildString {
                append(it.groupId)
                append(":")
                append(it.artifactId)
                append(":")
                append(it.version)
            }
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
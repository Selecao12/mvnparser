package com.example.mvnparser.service.impl

import com.example.mvnparser.service.FeignClient
import com.example.mvnparser.service.ParserProcessor
import org.springframework.stereotype.Service

@Service
class ParserProcessorImpl(
    private val feignClient: FeignClient, // todo перенести в RequestServiceImpl, чтобы там маппить строку в data class?
) : ParserProcessor {
    override fun process(groupId: String, artifactId: String, version: String): String? {
        // Получаем pom запрошенной зависимости
        // Проверяем есть ли тег parent
        // Получаем pom из тега parent, оттуда получаем версии зависимостей
        return feignClient.getPom(buildFilepath(groupId, artifactId, version))
    }

    private fun buildFilepath(groupId: String, artifactId: String, version: String): String {
        val group = groupId.replace('.', '/')
        val artifact = artifactId.replace('.', '/')
        return "$group/$artifact/$version/$artifact-$version.pom"
    }
}
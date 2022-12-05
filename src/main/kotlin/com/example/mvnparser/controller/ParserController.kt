package com.example.mvnparser.controller

import com.example.mvnparser.service.ParserProcessor
import com.example.mvnparser.service.XmlParser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class ParserController(
    private val xmlParser: XmlParser,
    private val parserProcessor: ParserProcessor,
) {

    @GetMapping("/v1/parse")
    fun parse(
        @RequestParam groupId: String,
        @RequestParam artifactId: String,
        @RequestParam version: String,
    ): ResponseEntity<String?> {
        return ResponseEntity.ok(parserProcessor.process(groupId, artifactId, version))
//        val response = WebClient.builder()
//            .baseUrl("https://repo1.maven.org/maven2/io/rest-assured/rest-assured/5.2.0/rest-assured-5.2.0.pom")
//            .build()
//            .get()
//            .retrieve()
//            .toEntity(String::class.java)
//            .block()
//            .let { requireNotNull(it) { "Response is null" } }
//        return ResponseEntity.ok(xmlParser.parse(requireNotNull(response.body) { "Request body is null" }))
    }
}

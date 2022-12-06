package com.example.mvnparser.controller

import com.example.mvnparser.service.ParserProcessor
import com.example.mvnparser.service.XmlParser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ParserController(
    private val parserProcessor: ParserProcessor,
) {

    @GetMapping("/v1/parse")
    fun parse(
        @RequestParam groupId: String,
        @RequestParam artifactId: String,
        @RequestParam version: String,
    ): ResponseEntity<List<String>> = ResponseEntity.ok(parserProcessor.process(groupId, artifactId, version))

    @PostMapping("/v1/parse-all")
    fun parse(@RequestBody implementations: List<String>): ResponseEntity<Set<String>> =
        ResponseEntity.ok(parserProcessor.processAll(implementations))
}

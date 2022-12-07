package com.example.mvnparser.controller

import com.example.mvnparser.service.ParserProcessor
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Parser Controller", description = "Contains methods for parsing dependencies from mvnrepository")
@RestController
class ParserController(
    private val parserProcessor: ParserProcessor,
) {
    @Operation(summary = "Parse implementation by groupId, artifactId, version")
    @GetMapping("/v1/parse")
    fun parse(
        @RequestParam groupId: String,
        @RequestParam artifactId: String,
        @RequestParam version: String,
    ): ResponseEntity<List<String>> = ResponseEntity.ok(parserProcessor.process(groupId, artifactId, version))

    @Operation(summary = "Parse list of implementations")
    @PostMapping("/v1/parse-all")
    fun parse(
        @RequestBody implementations: List<String>,
        @Parameter(description = "Return request implementations in response body")
        @RequestParam(defaultValue = "true") returnRequestImplementations: Boolean,
    ): ResponseEntity<Set<String>> =
        ResponseEntity.ok(parserProcessor.processAll(implementations, returnRequestImplementations))
}

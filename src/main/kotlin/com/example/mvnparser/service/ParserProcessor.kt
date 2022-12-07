package com.example.mvnparser.service

interface ParserProcessor {
    fun process(
        groupId: String,
        artifactId: String,
        version: String,
    ): List<String>

    fun processAll(implementations: List<String>, returnRequestImplementations: Boolean): Set<String>
}
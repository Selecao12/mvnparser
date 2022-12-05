package com.example.mvnparser.service

import org.w3c.dom.Document

interface ParserProcessor {
    fun process(
        groupId: String,
        artifactId: String,
        version: String,
    ): String?
}
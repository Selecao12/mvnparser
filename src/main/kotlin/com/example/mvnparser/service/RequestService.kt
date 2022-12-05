package com.example.mvnparser.service

import org.w3c.dom.Document

interface RequestService {
    fun sendSonatypeRequest(
        groupId: String,
        artifactId: String,
        version: String,
    ): String
}
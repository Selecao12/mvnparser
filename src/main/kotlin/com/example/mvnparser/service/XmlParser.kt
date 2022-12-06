package com.example.mvnparser.service

import com.example.mvnparser.model.domain.Pom

interface XmlParser {
    fun parse(xml: String): Pom
}

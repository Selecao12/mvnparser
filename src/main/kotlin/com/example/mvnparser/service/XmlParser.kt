package com.example.mvnparser.service

interface XmlParser {
    fun parse(xml: String): String?
}

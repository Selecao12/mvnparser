package com.example.mvnparser.service.impl

import com.example.mvnparser.service.XmlParser
import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

@Service
class XmlParserImpl : XmlParser {
    override fun parse(xml: String): String? {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isNamespaceAware = true

        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(InputSource(StringReader(xml)))

        val properties = parseProperties(document)

        return document.xmlEncoding
    }

    fun parseToDto(xml: String) {

    }

    private fun parseProperties(properties: Document): Map<String, String> {
        return mapOf()
    }

    private fun parseDependencies(document: Document) {
        
    }
}

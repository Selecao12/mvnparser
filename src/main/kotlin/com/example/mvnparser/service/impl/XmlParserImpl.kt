package com.example.mvnparser.service.impl

import com.example.mvnparser.model.domain.Pom
import com.example.mvnparser.service.XmlParser
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.stereotype.Service
import org.w3c.dom.Document

@Service
class XmlParserImpl : XmlParser {

    private val xmlMapper = XmlMapper(JacksonXmlModule().apply { setDefaultUseWrapper(false) })
        .registerKotlinModule()
        .apply {
            enable(SerializationFeature.INDENT_OUTPUT)
//            disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        }

    override fun parse(xml: String): Pom {
        return xmlMapper.readValue(xml, Pom::class.java)
    }
}

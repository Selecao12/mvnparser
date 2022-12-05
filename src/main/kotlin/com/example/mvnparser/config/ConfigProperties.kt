package com.example.mvnparser.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mvnparse")
data class ConfigProperties(
    var apiUrl: String = "",
)
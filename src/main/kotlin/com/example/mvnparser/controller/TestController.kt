package com.example.mvnparser.controller

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit

@RestController
class TestController {

    @GetMapping("/v1/get")
    fun get(): ResponseEntity<String>? = WebClient.builder()
        .baseUrl(UriComponentsBuilder.fromHttpUrl("https://jsonplaceholder.typicode.com/posts/1").toUriString())
        .build()
        .method(HttpMethod.GET)
        .apply {
            null?.let {
                bodyValue("{}")
            }
        }
        .retrieve().toEntity(String::class.java)
        .block()

    @GetMapping("/v1/post")
    fun post(): ResponseEntity<String>? = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(buildHttpClient(30000)))
        .baseUrl(UriComponentsBuilder.fromHttpUrl("https://jsonplaceholder.typicode.com/posts").build().toUriString())
        .build()
        .method(HttpMethod.POST)
        .bodyValue(
            """
            {
            "title": "foo",
            "body": "bar",
            "userId": 1
            }
        """.trimIndent()
        )
        .header("requestId", UUID.randomUUID().toString())
        .retrieve().toEntity(String::class.java)
        .block()

    private fun buildHttpClient(timeout: Int): HttpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
            .responseTimeout(Duration.ofMillis(timeout.toLong()))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(timeout.toLong(), TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(timeout.toLong(), TimeUnit.MILLISECONDS))
            }
}

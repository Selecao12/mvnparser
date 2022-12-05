package com.example.mvnparser.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "sonatype", url = "https://search.maven.org/")
interface FeignClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/remotecontent"])
    fun getPom(@RequestParam filepath: String): String

}
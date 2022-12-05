package com.example.mvnparser.service.impl

import com.example.mvnparser.service.FeignClient
import com.example.mvnparser.service.RequestService
import org.springframework.stereotype.Service
import org.w3c.dom.Document

@Service
class RequestServiceImpl(
    private val feignClient: FeignClient,
) : RequestService {
    override fun sendSonatypeRequest(groupId: String, artifactId: String, version: String): String {
        return feignClient.getPom(groupId)
    }
}
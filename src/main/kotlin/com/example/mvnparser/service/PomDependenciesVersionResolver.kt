package com.example.mvnparser.service

import com.example.mvnparser.model.domain.Pom

interface PomDependenciesVersionResolver {
    fun resolve(poms: MutableList<Pom>): List<Pom.Dependency>
}
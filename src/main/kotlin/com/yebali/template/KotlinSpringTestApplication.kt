package com.yebali.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class KotlinSpringTestApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringTestApplication>(*args)
}

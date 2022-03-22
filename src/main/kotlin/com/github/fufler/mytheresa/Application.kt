package com.github.fufler.mytheresa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import kotlin.system.exitProcess

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(Application::class.java)
                .run(*args)
    } catch (e: Exception) {
        e.printStackTrace(System.err)
        exitProcess(-1)
    }
}
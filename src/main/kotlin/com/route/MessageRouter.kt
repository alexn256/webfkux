package com.route

import com.repository.MessageRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import reactor.kotlin.core.publisher.toMono

@Configuration
open class MessageRouter(private val repository: MessageRepository) {

    @Bean
    open fun messageRoute(): RouterFunction<ServerResponse> = router {
        GET("/messages")  {
            ServerResponse.ok().body(
                repository.getAll()
            )
        }
        GET("/message") {
            val id = it.queryParam("id").map { it.toLong() }.orElse(3L)
            ServerResponse.ok().body(
                repository.getById(id)
            )
        }
        DELETE("/message/{id}") {
            val id = it.pathVariable("id").toLong()
            ServerResponse.ok().body(
                repository.delete(id)
            )
        }
        POST("/message") {
            it.bodyToMono(String::class.java).doOnNext { data ->
                ServerResponse.ok().body(
                    repository.create(data)
                )
            }.then(ok().build())
        }
    }
}
package com.repository

import com.model.Message
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MessageRepository {

    private var messages = mutableMapOf<Long, Message>()

    init {
        val text = MessageRepository::class.java.getResource("/messages.txt").readText()
        messages = text.split("\r\n").toList().map {
            val data = it.split("=")
            val id = data[0].toLong()
            id to Message(id, data[1])
        }.toMap() as MutableMap<Long, Message>
    }

    fun getById(id:Long):Mono<Message> {
        return Mono.just(messages[id])
    }

    fun getAll():Flux<Message> {
        return Flux.fromStream(messages.values.stream())
    }

    fun create(data:String):Mono<Long> {
        val id = (messages.size + 1).toLong()
        messages[id] = Message(id, data)
        return Mono.just(id)
    }

    fun delete(id:Long):Flux<Message> {
        messages.remove(id)
        return Flux.fromStream(messages.values.stream())
    }
}
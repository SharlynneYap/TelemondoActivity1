package com.TelemondoActivity1.TelemondoActivity1.messaging

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsConfig(
    @Value("\${nats.url:nats://localhost:4222}")
    private val url: String
) {
    @Bean(destroyMethod = "close")
    fun natsConnection(): Connection = Nats.connect(url)
}

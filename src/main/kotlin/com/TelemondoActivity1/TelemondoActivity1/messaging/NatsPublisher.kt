package com.TelemondoActivity1.TelemondoActivity1.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration

@Component
class NatsPublisher(
    private val nats: Connection,
) {
    private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()
    fun publish(subject: String, payload: Any) {
        val json = objectMapper.writeValueAsString(payload)
        nats.publish(subject, json.toByteArray(StandardCharsets.UTF_8))
        nats.flush(Duration.ofSeconds(1))
    }
}

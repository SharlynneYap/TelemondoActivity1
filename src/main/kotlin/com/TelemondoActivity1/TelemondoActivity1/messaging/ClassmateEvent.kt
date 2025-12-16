package com.TelemondoActivity1.TelemondoActivity1.messaging.events

import java.time.Instant
import java.util.UUID

data class ClassmateEventDto(
    val event: String,
    val timestamp: Instant,
    val entity: String,
    val data: ClassmateEventDataDto
)

data class ClassmateEventDataDto(
    val id: UUID?,
    val name: String,
    val age: Int?
)
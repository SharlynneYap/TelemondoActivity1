package com.TelemondoActivity1.TelemondoActivity1

import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.core.api.configuration.Orthography
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Import(TestContainersConfiguration::class)
@ImportTestcontainers
@DBRider
@DBUnit(
    schema = "telemondoactivity1_db",
    caseInsensitiveStrategy = Orthography.LOWERCASE,
    allowEmptyFields = true,
    alwaysCleanBefore = true,
    alwaysCleanAfter = true
)
@DataSet(
    value = [
        "db/datasets/classmate.xml"
    ]
)
@SpringBootTest
abstract class TestContainersBaseTest{

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun registerProps(registry: DynamicPropertyRegistry) {
            val nats = TestContainersConfiguration.natsContainer
            if (!nats.isRunning) nats.start()

            registry.add("nats.url") {
                "nats://${nats.host}:${nats.getMappedPort(4222)}"
            }
        }
    }
}
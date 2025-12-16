import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
@ImportTestcontainers
class TestContainersConfiguration {

    companion object {
        val natsContainer: GenericContainer<*> =
            GenericContainer(DockerImageName.parse("nats:2.12.0-alpine"))
                .withExposedPorts(4222)
    }

    @Bean
    @ServiceConnection
    fun mysqlContainer(): MySQLContainer<*> {
        return MySQLContainer("mysql:8.0.33").apply {
            withDatabaseName("telemondoactivity1_db")
            withUsername("test")
            withPassword("test")
            withCommand("mysqld --log-bin-trust-function-creators=1")
            //withReuse(true)
        }
    }

    @Bean
    fun natsContainerBean(): GenericContainer<*> = natsContainer
}

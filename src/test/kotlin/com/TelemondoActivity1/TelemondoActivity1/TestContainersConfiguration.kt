import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MySQLContainer

@TestConfiguration
@ImportTestcontainers
class TestContainersConfiguration {
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
}

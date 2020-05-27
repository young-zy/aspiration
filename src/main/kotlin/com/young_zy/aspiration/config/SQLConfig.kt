package com.young_zy.aspiration.config

import com.young_zy.aspiration.config.properties.SQLProperties
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.pool.PoolingConnectionFactoryProvider.*
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Duration

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
class SQLConfig(@Autowired final val sqlProperties: SQLProperties): AbstractR2dbcConfiguration() {

    private val connectionFactoryBuild = ConnectionFactories.get(
            builder()
                    .option(DRIVER, "pool")
                    .option(PROTOCOL, "mysql")
                    .option(HOST, sqlProperties.host)
                    .option(USER, sqlProperties.username)
                    .option(PASSWORD, sqlProperties.password)
                    .option(MAX_SIZE, sqlProperties.maxConnection)
                    .option(DATABASE, sqlProperties.database)
                    .build()
    )

    @Qualifier("connectionFactory")
    @Autowired lateinit var connectionFactory:ConnectionFactory

    @Bean
    override fun connectionFactory(): ConnectionFactory{
        return ConnectionPool(
                ConnectionPoolConfiguration.builder(connectionFactoryBuild)
                        .maxIdleTime(Duration.ofMinutes(30))
                        .initialSize(5)
                        .maxSize(30)
                        .maxCreateConnectionTime(Duration.ofSeconds(1))
                        .build()
        )
    }

    @Bean
    fun transactionManager(): R2dbcTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun r2dbcDatabaseClient(): DatabaseClient{
        return DatabaseClient.create(connectionFactory)
    }
}
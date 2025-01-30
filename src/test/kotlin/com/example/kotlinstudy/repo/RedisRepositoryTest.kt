package com.example.kotlinstudy.repo

import com.example.kotlinstudy.domain.InMemoryRepository
import com.example.kotlinstudy.setup.TestRedisConfiguration
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@Import(TestRedisConfiguration::class)
@SpringBootTest
class RedisRepositoryTest {
    @Autowired
    private lateinit var redisRepositoryImpl: InMemoryRepository

    private val log = KotlinLogging.logger { }

    @Test
    fun setup() {
        log.info { "test setup => {$this.redisRepositoryImpl}"}
    }

    @Test
    fun redisTest() {
        log.info { "redis test start" }
        val numberOfThreads = 10

        val service = Executors.newFixedThreadPool(10)
        val latch = CountDownLatch(numberOfThreads)

        for (index in 1..numberOfThreads) {
            service.submit {
                this.redisRepositoryImpl.save(index.toString(), index)
                log.info { "save $index" }
                latch.countDown()
            }
            //this.redisRepositoryImpl.save(index.toString(), index)//.toString())
        }

        latch.await()

        val value = this.redisRepositoryImpl.findByKey(1.toString())

        println(value)

        val results = this.redisRepositoryImpl.findAll()

        for (result in results) {
            log.info { "result = $result" }
            println("result ====$result")
        }

        Assertions.assertThat(results.size).isEqualTo(numberOfThreads)
    }
}
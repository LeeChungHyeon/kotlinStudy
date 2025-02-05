package com.example.kotlinstudy.config.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import io.findify.s3mock.S3Mock
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmbedeedS3Config {

    private val log = KotlinLogging.logger {}

    val port = 7777
    private val api = S3Mock.Builder().withPort(port).withInMemoryBackend().build()

    private val serviceEndpoint = "http://localhost:7777"
    private val region = "us-west-2"

    @PostConstruct
    fun init() {
        log.info { "mocking S3 API start" }
        this.api.start()
    }

    @Bean
    fun amazonS3(): AmazonS3 {


        val endpoint = EndpointConfiguration(serviceEndpoint, region)
        val client: AmazonS3 = AmazonS3ClientBuilder
            .standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(endpoint)
            .withCredentials(AWSStaticCredentialsProvider(AnonymousAWSCredentials()))
            .build()

        log.info { "embedded amazonS3 === $client" }
        return client
    }

    @PreDestroy
    fun destroy() {
        log.info { "s3 mock api shutdown" }

        this.api.shutdown()
    }

}
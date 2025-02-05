package com.example.kotlinstudy.service.common

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths
import java.util.UUID

@Service
class LocalS3FileUploaderServiceImpl(
    private val amazonS3: AmazonS3
) : FileUploaderService {

    private val log = KotlinLogging.logger {}

    private val folderPath = "postImg/"
    private val bucket = "my-bucket"

    init {
        log.info { "amazonS3 == ${this.amazonS3}" }

        this.amazonS3.createBucket(bucket)
    }

    override fun upload(file: MultipartFile): String {

        val uuid = UUID.randomUUID().toString()
        val fileName = folderPath + uuid + "_" + file.originalFilename

        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = file.contentType
        objectMetadata.contentLength = file.size

        val putObjectRequest = PutObjectRequest(
            bucket,
            fileName,
            file.inputStream,
            objectMetadata
        )

        this.amazonS3.putObject(putObjectRequest)

        val url = this.amazonS3.getUrl(bucket, fileName)

        return url.toString()

    }
}
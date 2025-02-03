package com.example.kotlinstudy.service.common

import org.springframework.web.multipart.MultipartFile

interface FileUploaderService {

    fun upload(file: MultipartFile): String

}
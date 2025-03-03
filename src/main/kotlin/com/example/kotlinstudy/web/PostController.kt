package com.example.kotlinstudy.web

import com.example.kotlinstudy.domain.post.PostSaveReq
import com.example.kotlinstudy.service.PostService
import com.example.kotlinstudy.util.dto.SearchCondition
import com.example.kotlinstudy.util.value.CmResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable, searchCondition: SearchCondition): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Posts", postService.findPosts(pageable, searchCondition))
    }


    @GetMapping("/post/{id}")
    fun findById(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "find post by Id", postService.findPostById(id))
    }

    @DeleteMapping("/post/{id}")
    fun delete(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "delete post by Id", postService.deletePost(id))
    }

    @PostMapping("/post")
    fun save(@Valid @RequestBody dto: PostSaveReq): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "save post", postService.savePost(dto))
    }

    @PostMapping("/post/img")
    fun savePostImg(image: MultipartFile): CmResDto<*> {
        return CmResDto(HttpStatus.OK,"save post img", postService.savePostImg(image))
    }
}
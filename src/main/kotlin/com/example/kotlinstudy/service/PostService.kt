package com.example.kotlinstudy.service

import com.example.kotlinstudy.domain.post.*
import com.example.kotlinstudy.util.dto.SearchCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {

    @PreAuthorize("hasRole('USER')")
    //Secured는 preAuthorize와 같은거지만 표현식의 차이가 있기 때문에 정교하게 사용이 가능한 preAuthorize를 사용
    //@Secured(*["ROLE_SUPER", "ROLE_ADMIN"])
    @Transactional(readOnly = true)
    fun findPosts(pageable: Pageable, searchCondition: SearchCondition): Page<PostRes> {
        return postRepository.findPosts(pageable, searchCondition).map { it.toDto() }
    }


    @Transactional
    fun savePost(dto: PostSaveReq): PostRes {
        return postRepository.save(dto.toEntity()).toDto()
    }

    @Transactional
    fun deletePost(id: Long) {
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id: Long): PostRes {
        return postRepository.findById(id).orElseThrow().toDto()
    }

}
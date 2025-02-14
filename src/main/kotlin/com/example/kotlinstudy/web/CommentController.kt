package com.example.kotlinstudy.web

import com.example.kotlinstudy.domain.comment.CommentRes
import com.example.kotlinstudy.domain.comment.CommentSaveReq
import com.example.kotlinstudy.service.CommentService
import com.example.kotlinstudy.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comment")
    fun saveComment(dto: CommentSaveReq): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "save comment", commentService.saveComment(dto))
    }

    @GetMapping("/comment/{idAncestor}")
    fun findCommentByAncestorComment(@PathVariable idAncestor: Long): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find comment By idAncestor", commentService.findCommentByAncestorComment(idAncestor))
    }

}
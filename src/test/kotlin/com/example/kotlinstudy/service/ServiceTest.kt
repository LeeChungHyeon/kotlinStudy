package com.example.kotlinstudy.service

import com.example.kotlinstudy.domain.comment.CommentRepository
import com.example.kotlinstudy.domain.comment.CommentSaveReq
import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.post.Post
import com.example.kotlinstudy.domain.post.PostRepository
import com.example.kotlinstudy.setup.MockitoHelper
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ServiceTest {


  private val log = KotlinLogging.logger {}

  @Mock //mocking용 객체, 테스트 런타임시 주입것
  private lateinit var commentRepository: CommentRepository

  @Mock
  private lateinit var postRepository: PostRepository

  @InjectMocks
  private lateinit var commentService: CommentService

  @Test
  fun mockDiTest() {
   log.info { """
    ${this.commentService}
   """.trimIndent()}
  }


  @Test
  fun saveCommentTest() {

    //given
    val dto = CommentSaveReq(
      memberId = 1,
      content = "test content",
      postId = 1,
      idAncestor = null
    )

    val post = Optional.ofNullable(
      Post(
        title = "title",
        content = "content",
        member = Member.createFakeMember(2),
        createAt = LocalDateTime.now(),
        updateAt = LocalDateTime.now()
      )
    )

    val expectedPost = post.orElseThrow()
    val comment = dto.toEntity(expectedPost)

    // stub
    Mockito.`when`(postRepository.findById(dto.postId)).thenReturn(post)
    Mockito.`when`(commentRepository.saveComment(MockitoHelper.anyObject())).thenReturn(comment)
    Mockito.`when`(commentRepository.saveCommentClouser(0, dto.idAncestor)).thenReturn(anyInt())

    val saveComment = commentService.saveComment(dto)

    // then
    Assertions.assertEquals(comment.content, saveComment.content)
    Assertions.assertEquals(comment.member.id, saveComment.member.id)


  }
}
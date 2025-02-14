package com.example.kotlinstudy

import com.example.kotlinstudy.domain.comment.Comment
import com.example.kotlinstudy.domain.comment.CommentRepository
import com.example.kotlinstudy.domain.comment.CommentSaveReq
import com.example.kotlinstudy.service.CommentService
import com.example.kotlinstudy.setup.TestRedisConfiguration
import net.okihouse.autocomplete.repository.AutocompleteRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [TestRedisConfiguration::class])
class KotlinStudyApplicationTests(
	@Autowired
	val df: DefaultListableBeanFactory
) {

	@Autowired
	private lateinit var commentService: CommentService

	@Autowired
	private lateinit var commentRepository: CommentRepository

	@Autowired
	private lateinit var autocompleteRepository: AutocompleteRepository

	@Test
	fun contextLoads() {

	}

	@Test
	fun autoComplteTest() {
		val apple = "apple"


//		autocompleteRepository.clear(apple)

		autocompleteRepository.add(apple)

		val autocompletes = autocompleteRepository.complete("a")

		Assertions.assertTrue(autocompletes.size == 1)

		val autocompleteData = autocompletes[0]

		Assertions.assertTrue(autocompleteData.value == apple)
		Assertions.assertTrue(autocompleteData.score == 1)
	}


	@Test
	fun saveCommentTest() {
		val dto = CommentSaveReq(
			memberId = 2,
			content = "test content3",
			postId = 1,
			idAncestor = 2
		)
		commentService.saveComment(dto)
	}



	@Test
	fun printBeannames() {
		val names = df.beanDefinitionNames

		for (name in names) {
			val bean = df.getBean(name)
			println("name = $name object = $bean")
		}
	}

}

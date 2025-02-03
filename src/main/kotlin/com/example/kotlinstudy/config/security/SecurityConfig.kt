package com.example.kotlinstudy.config.security

import com.example.kotlinstudy.domain.HashMapRepositoryImpl
import com.example.kotlinstudy.domain.InMemoryRepository
import com.example.kotlinstudy.domain.RedisRepositoryImpl
import com.example.kotlinstudy.domain.member.MemberRepository
import com.example.kotlinstudy.util.Script
import com.example.kotlinstudy.util.value.CmResDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.util.matcher.IpAddressMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    private val log = KotlinLogging.logger {}

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer {
            web: WebSecurity -> web.ignoring().requestMatchers("/**")
        }
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .formLogin { formLogin -> formLogin.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { cors -> cors.configurationSource(corsConfig()) }
            .addFilter(loginFilter())
            .addFilter(authenticationFilter())
            .addFilterAt(authenticationFilter(), AuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.accessDeniedHandler(CustomAccessDeniedHandler())
                exceptionHandling.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
//            .authorizeHttpRequests { response ->
//                response.requestMatchers("/**").authenticated()
//                // 모든 요청에 대해서 인증없이 허용
//                //response.anyRequest().permitAll()
//            }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/v1/posts").hasAnyRole("USER", "ADMIN")
                //ip 기반 인가처리
                //auth.requestMatchers(IpAddressMatcher("192.168.0.1")).hasRole("ADMIN")
                auth.anyRequest().permitAll()
            }
            .logout { logout ->
                logout.logoutUrl("/logout")
                logout.logoutSuccessHandler(CustomLogoutSuccessHandler(objectMapper))
            }

        return http.build()
    }

    class CustomLogoutSuccessHandler(
        private val objectMapper: ObjectMapper,
    ) : LogoutSuccessHandler {

        private val log = KotlinLogging.logger {}

        override fun onLogoutSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication?
        ) {
            log.info { "logout success" }

            val context = SecurityContextHolder.getContext()
            context.authentication = null
            SecurityContextHolder.clearContext()

            val cmResDto = CmResDto(HttpStatus.OK, "logout success", null)
            Script.responseData(response, objectMapper.writeValueAsString(cmResDto))
        }
    }

    class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

        private val log = KotlinLogging.logger {}

        override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            authException: AuthenticationException?
        ) {
            log.info { "!!access denied" }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
        }
    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {

        private val log = KotlinLogging.logger {}

        override fun handle(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { "access denied" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
        }
    }

    class CustomFailureHandler : AuthenticationFailureHandler {

        private val log = KotlinLogging.logger {}

        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            exception: AuthenticationException?
        ) {
            log.warn { "로그인 실패" }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 실패")
        }
    }

    class CustomSuccessHandler : AuthenticationSuccessHandler {

        private val log = KotlinLogging.logger {}

        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?
        ) {
            log.info { "login Success" }
        }
    }

    @Bean
    fun inmemoryRepository(): InMemoryRepository {
        return RedisRepositoryImpl(this.redisTemplate)
    }


    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository,
            objectMapper = objectMapper,
            memoryRepository = inmemoryRepository()
        )
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): CustomUserNameAuthenticationFilter {

        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper, inmemoryRepository())
        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())

        return authenticationFilter
    }

    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("Authorization")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }



}
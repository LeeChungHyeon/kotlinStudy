package com.example.kotlinstudy.config.security

import com.example.kotlinstudy.domain.member.Member
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails (
    member: Member
) : UserDetails {
    private val log = KotlinLogging.logger {}

    var member: Member = member
        private set


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info { "Role 검증" }
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "ROLE_" +  member.role })

        return collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.UserType
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserType, Long> {
    fun findByUsername(username: String): UserType?
    fun findByUsernameAndActive(username: String, active: Boolean): UserType?
}
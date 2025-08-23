package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.UserType
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserType, Long> {
    fun findByUsernameAndActive(username: String, active: Boolean): UserType?
}
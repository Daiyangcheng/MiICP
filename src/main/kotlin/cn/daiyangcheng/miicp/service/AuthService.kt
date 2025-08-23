package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.type.UserType
import org.springframework.stereotype.Service

@Service
interface AuthService {
    fun login(username: String, password: String): String?
    fun validateToken(token: String): UserType?
    fun generateToken(user: UserType): String
}
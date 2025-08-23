package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.repository.UserRepository
import cn.daiyangcheng.miicp.repository.TokenRepository
import cn.daiyangcheng.miicp.type.UserType
import cn.daiyangcheng.miicp.type.TokenType
import jakarta.annotation.Resource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import jakarta.transaction.Transactional
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Service
class AuthServiceImpl : AuthService {

    @Resource
    private lateinit var userRepository: UserRepository

    @Resource
    private lateinit var tokenRepository: TokenRepository
    
    @Resource
    private lateinit var passwordEncoder: PasswordEncoder

    @Transactional
    override fun login(username: String, password: String): String? {
        val user = userRepository.findByUsernameAndActive(username, true)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            // 删除用户的旧token
            tokenRepository.deleteByUserId(user.id!!)
            generateToken(user)
        } else null
    }

    override fun validateToken(token: String): UserType? {
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val tokenEntity = tokenRepository.findByTokenAndNotExpired(token, now)
        return if (tokenEntity != null) {
            userRepository.findById(tokenEntity.userId!!).orElse(null)
        } else null
    }

    override fun generateToken(user: UserType): String {
        val token = UUID.randomUUID().toString()
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val expiresAt = now.plusSeconds(86400) // 24小时后过期
        
        val tokenEntity = TokenType().apply {
            this.token = token
            this.userId = user.id
            this.createdAt = now
            this.expiresAt = expiresAt
        }
        
        tokenRepository.save(tokenEntity)
        return token
    }
}
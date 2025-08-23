package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.TokenType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface TokenRepository : JpaRepository<TokenType, Long> {
    @Query("SELECT t FROM TokenType t WHERE t.token = ?1 AND t.expiresAt > ?2")
    fun findByTokenAndNotExpired(token: String, now: Instant): TokenType?
    
    @Modifying
    @Query("DELETE FROM TokenType t WHERE t.userId = ?1")
    fun deleteByUserId(userId: Long)
    
    @Modifying
    @Query("DELETE FROM TokenType t WHERE t.expiresAt < ?1")
    fun deleteExpiredTokens(now: Instant)
}
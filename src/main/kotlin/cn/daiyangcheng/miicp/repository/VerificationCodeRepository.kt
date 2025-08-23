package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.VerificationCodeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface VerificationCodeRepository : JpaRepository<VerificationCodeType, Long> {
    fun findByEmailAndCodeAndExpiresAtAfter(email: String, code: String, now: Instant): VerificationCodeType?
    
    @Modifying
    @Query("DELETE FROM VerificationCodeType v WHERE v.email = ?1")
    fun deleteByEmail(email: String)
}
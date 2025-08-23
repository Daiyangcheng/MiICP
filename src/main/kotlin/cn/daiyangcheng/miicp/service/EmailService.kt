package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.repository.VerificationCodeRepository
import cn.daiyangcheng.miicp.type.VerificationCodeType
import jakarta.annotation.Resource
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.core.io.ClassPathResource
import jakarta.mail.internet.MimeMessage
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random

@Service
class EmailService(private val mailSender: JavaMailSender) {

    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    @Resource
    private lateinit var verificationCodeRepository: VerificationCodeRepository

    @Transactional
    fun sendVerificationCode(email: String): Result<String> {
        val code = Random.nextInt(100000, 999999).toString()
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val expiresAt = now.plusSeconds(300) // 5分钟后过期

        // 删除该邮箱的旧验证码
        verificationCodeRepository.deleteByEmail(email)

        // 保存新验证码
        val verificationCode = VerificationCodeType().apply {
            this.email = email
            this.code = code
            this.createdAt = now
            this.expiresAt = expiresAt
        }
        verificationCodeRepository.save(verificationCode)

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")
        
        helper.setFrom(fromEmail)
        helper.setTo(email)
        helper.setSubject("ICP申请验证码")
        
        val htmlTemplate = ClassPathResource("templates/verification-email.html")
            .inputStream.bufferedReader().use { it.readText() }
        val htmlContent = htmlTemplate.replace("\${code}", code)
        
        helper.setText(htmlContent, true)
        
        return try {
            mailSender.send(mimeMessage)
            Result.success(code)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Transactional
    fun verifyCode(email: String, code: String): Boolean {
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val verificationCode = verificationCodeRepository.findByEmailAndCodeAndExpiresAtAfter(email, code, now)
        if (verificationCode != null) {
            verificationCodeRepository.deleteByEmail(email)
            return true
        }
        return false
    }
}
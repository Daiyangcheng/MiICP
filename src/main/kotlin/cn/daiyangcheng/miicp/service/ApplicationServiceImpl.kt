package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.repository.ApplicationRepository
import cn.daiyangcheng.miicp.type.ApplicationType
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.core.io.ClassPathResource
import org.springframework.beans.factory.annotation.Value
import jakarta.mail.internet.MimeMessage
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.time.ZoneId

@Service
class ApplicationServiceImpl : ApplicationService {

    @Autowired
    private lateinit var icpNumService: IcpNumService

    @Autowired
    private lateinit var applicationRepository: ApplicationRepository
    
    @Autowired
    private lateinit var mailSender: JavaMailSender
    
    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    override fun createApplication(email: String, name: String, domain: String, description: String?, num: String): ApplicationType {
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val application = ApplicationType().apply {
            this.email = email
            this.name = name
            this.domain = domain
            this.description = description
            this.createdAt = now
            this.updatedAt = now
            this.status = "PENDING"
            this.num = num
        }
        return applicationRepository.save(application)
    }

    override fun getAllApplications(pageable: Pageable): Page<ApplicationType> {
        return applicationRepository.findAll(pageable)
    }

    override fun approveApplication(id: Long): ApplicationType {
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val application = applicationRepository.findById(id)
            .orElseThrow { RuntimeException("Application not found") }
        application.status = "APPROVED"
        application.updatedAt = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val savedApplication = applicationRepository.save(application)
        val saveIcpNum = icpNumService.findByNum(application.num!!)
        saveIcpNum.apply {
            this?.email = application.email
            this?.description = application.description
            this?.domain = application.domain
            this?.name = application.name
            this?.createdAt = now
            this?.updatedAt = now
            this?.active = true
        }
        icpNumService.update(saveIcpNum!!)
        sendNotificationEmail(application, "APPROVED")
        return savedApplication
    }

    override fun rejectApplication(id: Long): ApplicationType {
        val application = applicationRepository.findById(id)
            .orElseThrow { RuntimeException("Application not found") }
        application.status = "REJECTED"
        application.updatedAt = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        val savedApplication = applicationRepository.save(application)
        
        sendNotificationEmail(application, "REJECTED")
        return savedApplication
    }

    override fun getApplicationById(id: Long): ApplicationType? {
        return applicationRepository.findById(id).orElse(null)
    }

    override fun findByNum(num: String): ApplicationType? {
        return applicationRepository.findByNum(num)
    }

    private fun sendNotificationEmail(application: ApplicationType, status: String) {
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")
            
            helper.setFrom(fromEmail)
            helper.setTo(application.email!!)
            
            val templateName = if (status == "APPROVED") "application-approved.html" else "application-rejected.html"
            val subject = if (status == "APPROVED") "ICP申请审核通过" else "ICP申请审核未通过"
            
            helper.setSubject(subject)
            
            val htmlTemplate = ClassPathResource("templates/$templateName")
                .inputStream.bufferedReader().use { it.readText() }
            val htmlContent = htmlTemplate
                .replace("\${domain}", application.domain ?: "")
                .replace("\${applicationId}", application.id.toString())
            
            helper.setText(htmlContent, true)
            mailSender.send(mimeMessage)
        } catch (e: Exception) {
            // 记录日志但不影响主流程
            println("Failed to send notification email: ${e.message}")
        }
    }
}
package cn.daiyangcheng.miicp.controller

import cn.daiyangcheng.miicp.annotation.Limit
import cn.daiyangcheng.miicp.service.ApplicationService
import cn.daiyangcheng.miicp.service.EmailService
import cn.daiyangcheng.miicp.service.IcpNumService
import cn.daiyangcheng.miicp.type.IcpNumType
import cn.daiyangcheng.miicp.utils.ResponseBuilder
import jakarta.annotation.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@RestController
class IcpController {

    @Resource
    private lateinit var icpNumService: IcpNumService
    
    @Resource
    private lateinit var applicationService: ApplicationService
    
    @Resource
    private lateinit var emailService: EmailService

    private var builder: ResponseBuilder = ResponseBuilder()

    @GetMapping("/icp")
    fun getIcpList(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "100") size: Int
    ): ResponseEntity<Map<String, Any?>> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val currentYearActivePage = icpNumService.findCurrentYearActive(pageable)
        
        val ListNums = currentYearActivePage.content.map { it.num!! }
        
        data class Response (
            val nums: List<String>,
            val totalElements: Long,
            val totalPages: Int,
            val currentPage: Int,
            val size: Int
        )

        val rs = Response(
            nums = ListNums,
            totalElements = currentYearActivePage.totalElements,
            totalPages = currentYearActivePage.totalPages,
            currentPage = currentYearActivePage.number + 1,
            size = currentYearActivePage.size
        )

        return builder.success(data = builder.toMap(rs))
    }

    @PostMapping("/icp/send-code")
    @Limit(key = "#email", period = 60, count = 1)
    fun sendVerificationCode(@RequestParam("email") email: String): ResponseEntity<Map<String, Any?>> {
        if (email.isEmpty()) {
            return builder.badRequest("请提供邮箱")
        }
        
        val result = emailService.sendVerificationCode(email)
        return if (result.isSuccess) {
            builder.success(message = "验证码已发送")
        } else {
            val error = result.exceptionOrNull()
            builder.exception(message = "邮件发送失败: ${error?.message ?: "未知错误"}")
        }
    }

    @PostMapping("/icp/apply")
    fun applyIcp(
        @RequestParam("email") email: String,
        @RequestParam("name") name: String,
        @RequestParam("domain") domain: String,
        @RequestParam("description") description: String?,
        @RequestParam("code") code: String
    ): ResponseEntity<Map<String, Any?>> {
        if (!emailService.verifyCode(email, code)) {
            return builder.badRequest(message = "验证码错误")
        }
        
        val application = applicationService.createApplication(
            email = email,
            name = name,
            domain = domain,
            description = description
        )
        return builder.success(data = builder.toMap(application))
    }
}
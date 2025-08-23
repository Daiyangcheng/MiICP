package cn.daiyangcheng.miicp.controller

import cn.daiyangcheng.miicp.annotation.NeedAuthorization
import cn.daiyangcheng.miicp.service.ApplicationService
import cn.daiyangcheng.miicp.type.ApplicationType
import cn.daiyangcheng.miicp.utils.ResponseBuilder
import jakarta.annotation.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController {

    @Resource
    private lateinit var applicationService: ApplicationService

    private var builder: ResponseBuilder = ResponseBuilder()

    @GetMapping("/applications")
    @NeedAuthorization
    fun getApplications(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Map<String, Any?>> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val applicationsPage = applicationService.getAllApplications(pageable)
        
        data class Response(
            val applications: List<ApplicationType>,
            val totalElements: Long,
            val totalPages: Int,
            val currentPage: Int,
            val size: Int
        )

        val rs = Response(
            applications = applicationsPage.content,
            totalElements = applicationsPage.totalElements,
            totalPages = applicationsPage.totalPages,
            currentPage = applicationsPage.number + 1,
            size = applicationsPage.size
        )

        return builder.success(data = builder.toMap(rs))
    }

    @GetMapping("/applications/{id}")
    @NeedAuthorization
    fun getApplication(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        val application = applicationService.getApplicationById(id)
            ?: return builder.notFound(message = "申请不存在")
        
        return builder.success(data = builder.toMap(application))
    }

    @PostMapping("/applications/{id}/approve")
    @NeedAuthorization
    fun approveApplication(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        return try {
            val application = applicationService.approveApplication(id)
            builder.success(data = builder.toMap(application), message = "申请已同意")
        } catch (e: RuntimeException) {
            builder.notFound(message = e.message ?: "申请不存在")
        }
    }

    @PostMapping("/applications/{id}/reject")
    @NeedAuthorization
    fun rejectApplication(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        return try {
            val application = applicationService.rejectApplication(id)
            builder.success(data = builder.toMap(application), message = "申请已拒绝")
        } catch (e: RuntimeException) {
            builder.notFound(message = e.message ?: "申请不存在")
        }
    }
}
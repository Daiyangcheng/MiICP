package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.type.ApplicationType
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
interface ApplicationService {
    fun createApplication(email: String, name: String, domain: String, description: String?): ApplicationType
    fun getAllApplications(pageable: Pageable): Page<ApplicationType>
    fun approveApplication(id: Long): ApplicationType
    fun rejectApplication(id: Long): ApplicationType
    fun getApplicationById(id: Long): ApplicationType?
}
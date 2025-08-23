package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.ApplicationType
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository : JpaRepository<ApplicationType, Long> {
}
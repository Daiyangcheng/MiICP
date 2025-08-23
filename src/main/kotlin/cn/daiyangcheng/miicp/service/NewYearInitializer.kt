package cn.daiyangcheng.miicp.service

import jakarta.annotation.Resource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class NewYearInitializer {

    @Resource
    private lateinit var icpNumService: IcpNumService

    @Scheduled(cron = "0 0 0 1 1 *") // 每年1月1日0点执行
    fun initializeNewYear() {
        val currentYear = LocalDate.now().year
        icpNumService.initializeNewYearNumbers(currentYear)
    }
}
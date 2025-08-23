package cn.daiyangcheng.miicp.service

import jakarta.annotation.PostConstruct
import jakarta.annotation.Resource
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer {

    @Resource
    private lateinit var icpNumService: IcpNumService


    // 首次执行时没有数据，填充初始数据
    @PostConstruct
    fun initializeData() {
        if (icpNumService.findAll().isEmpty()) {
            val currentYear = LocalDate.now().year
            icpNumService.initializeNewYearNumbers(currentYear)
        }
    }
}
package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.repository.IcpNumRepository
import cn.daiyangcheng.miicp.type.IcpNumType
import jakarta.annotation.Resource
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Service
class IcpNumServiceImpl : IcpNumService {

    @Resource
    private lateinit var icpNumRepository: IcpNumRepository

    override fun update(icpNumType: IcpNumType) {
        icpNumRepository.save(icpNumType)
    }

    override fun delete(icpNumType: IcpNumType) {
        icpNumRepository.delete(icpNumType)
    }

    override fun findAll(): List<IcpNumType> {
        return icpNumRepository.findAll()
    }

    override fun initializeNewYearNumbers(year: Int) {
        val startNum = year * 10000
        val endNum = startNum + 9999
        val now = Instant.now().atZone(ZoneId.of("UTC+8")).toInstant()
        
        val icpNumbers = (startNum..endNum).map { num ->
            IcpNumType().apply {
                this.num = num.toString()
                this.email = null
                this.createdAt = now
                this.updatedAt = now
                this.active = true
                this.notes = null
                this.description = null
            }
        }
        
        icpNumRepository.saveAll(icpNumbers)
    }

    override fun findCurrentYearActive(pageable: Pageable): Page<IcpNumType> {
        val currentYear = LocalDate.now().year
        return icpNumRepository.findByYearAndActive(currentYear.toString(), pageable)
    }
}
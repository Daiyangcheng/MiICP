package cn.daiyangcheng.miicp.service

import cn.daiyangcheng.miicp.type.IcpNumType
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
interface IcpNumService {
    fun update(icpNumType: IcpNumType)
    fun delete(icpNumType: IcpNumType)
    fun findAll(): List<IcpNumType>
    fun findByNum(num: String): IcpNumType?
    fun initializeNewYearNumbers(year: Int)
    fun findCurrentYearActive(pageable: Pageable): Page<IcpNumType>
}
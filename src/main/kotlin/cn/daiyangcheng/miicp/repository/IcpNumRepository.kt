package cn.daiyangcheng.miicp.repository

import cn.daiyangcheng.miicp.type.IcpNumType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IcpNumRepository : JpaRepository<IcpNumType, Long> {
    @Query("SELECT i FROM IcpNumType i WHERE i.num LIKE ?1% AND i.active = true")
    fun findByYearAndActive(yearPrefix: String, pageable: Pageable): Page<IcpNumType>
}
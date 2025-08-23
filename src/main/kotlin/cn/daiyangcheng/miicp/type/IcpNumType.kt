package cn.daiyangcheng.miicp.type

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Data
@Table(name = "icp_num", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
open class IcpNumType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "num", nullable = false)
    open var num: String? = null

    @Column(name = "email", nullable = true)
    open var email: String? = null

    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null

    @Column(name = "updated_at", nullable = false)
    open var updatedAt: Instant? = null

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    open var active: Boolean? = false

    @ColumnDefault("NULL")
    @Column(name = "notes")
    open var notes: String? = null

    @ColumnDefault("NULL")
    @Column(name = "description")
    open var description: String? = null
}
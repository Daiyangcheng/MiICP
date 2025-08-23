package cn.daiyangcheng.miicp.type

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Data
@Table(name = "application", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
open class ApplicationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
    @Column(name = "email", nullable = false)
    open var email: String? = null
    @Column(name = "name", nullable = false)
    open var name: String? = null
    @Column(name = "domain", nullable = false)
    open var domain: String? = null
    @ColumnDefault("NULL")
    @Column(name = "description")
    open var description: String? = null
    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null
    @Column(name = "updated_at", nullable = false)
    open var updatedAt: Instant? = null
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false)
    open var status: String? = "PENDING"
}
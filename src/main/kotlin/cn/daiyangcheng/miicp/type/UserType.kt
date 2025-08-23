package cn.daiyangcheng.miicp.type

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Data
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
open class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
    
    @Column(name = "username", nullable = false, unique = true)
    open var username: String? = null
    
    @Column(name = "password", nullable = false)
    open var password: String? = null
    
    @ColumnDefault("'ADMIN'")
    @Column(name = "role", nullable = false)
    open var role: String? = "ADMIN"
    
    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    open var active: Boolean? = true
}
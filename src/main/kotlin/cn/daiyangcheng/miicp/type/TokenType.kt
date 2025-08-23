package cn.daiyangcheng.miicp.type

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant

@Entity
@Data
@Table(name = "tokens", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
open class TokenType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null
    
    @Column(name = "token", nullable = false, unique = true)
    open var token: String? = null
    
    @Column(name = "user_id", nullable = false)
    open var userId: Long? = null
    
    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null
    
    @Column(name = "expires_at", nullable = false)
    open var expiresAt: Instant? = null
}
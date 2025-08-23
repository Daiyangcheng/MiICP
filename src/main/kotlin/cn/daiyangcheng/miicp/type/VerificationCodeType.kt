package cn.daiyangcheng.miicp.type

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.Instant

@Entity
@Data
@Table(name = "verification_code", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
open class VerificationCodeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "email", nullable = false)
    open var email: String? = null

    @Column(name = "code", nullable = false)
    open var code: String? = null

    @Column(name = "created_at", nullable = false)
    open var createdAt: Instant? = null

    @Column(name = "expires_at", nullable = false)
    open var expiresAt: Instant? = null
}
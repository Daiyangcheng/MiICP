package cn.daiyangcheng.miicp.controller

import cn.daiyangcheng.miicp.utils.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/util")
class UtilController {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private var builder: ResponseBuilder = ResponseBuilder()

    @GetMapping("/hash-password")
    fun hashPassword(@RequestParam("password") password: String): ResponseEntity<Map<String, Any?>> {
        val hashedPassword = passwordEncoder.encode(password)
        
        data class Response(
            val hashedPassword: String
        )

        val rs = Response(hashedPassword = hashedPassword)
        return builder.success(data = builder.toMap(rs))
    }
}
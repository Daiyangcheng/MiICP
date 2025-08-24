package cn.daiyangcheng.miicp.controller

import cn.daiyangcheng.miicp.service.AuthService
import cn.daiyangcheng.miicp.utils.ResponseBuilder
import jakarta.annotation.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController {

    @Resource
    private lateinit var authService: AuthService

    private var builder: ResponseBuilder = ResponseBuilder()

    @PostMapping("/login")
    fun login(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<Map<String, Any?>> {
        val token = authService.login(username, password)
        return if (token != null) {
            data class Response(val token: String)
            builder.success(data = builder.toMap(Response(token)), message = "登录成功")
        } else {
            builder.badRequest(message = "用户名或密码错误")
        }
    }
}
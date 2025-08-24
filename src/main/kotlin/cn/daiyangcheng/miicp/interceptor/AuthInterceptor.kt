package cn.daiyangcheng.miicp.interceptor

import cn.daiyangcheng.miicp.annotation.NeedAuthorization
import cn.daiyangcheng.miicp.service.AuthService
import cn.daiyangcheng.miicp.utils.ResponseBuilder
import com.alibaba.fastjson2.JSON
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor : HandlerInterceptor {

    @Resource
    private lateinit var authService: AuthService
    
    private val builder = ResponseBuilder()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        val needAuth = handler.getMethodAnnotation(NeedAuthorization::class.java)
        if (needAuth == null) {
            return true
        }

        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token.isNullOrBlank()) {
            response.status = 401
            // 解决中文乱码
            response.contentType = "application/json;charset=UTF-8";
            response.writer.write(JSON.toJSONString(builder.unauthorized("未提供授权 Token").body))
            return false
        }

        val user = authService.validateToken(token)
        if (user == null || user.role != needAuth.role) {
            response.status = 403
            // 解决中文乱码
            response.contentType = "application/json;charset=UTF-8";
            response.writer.write(JSON.toJSONString(builder.forbidden("无权限").body))
            return false
        }

        request.setAttribute("currentUser", user)
        return true
    }
}
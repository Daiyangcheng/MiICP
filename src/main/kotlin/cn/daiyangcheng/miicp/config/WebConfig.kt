package cn.daiyangcheng.miicp.config

import cn.daiyangcheng.miicp.interceptor.AuthInterceptor
import jakarta.annotation.Resource
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    @Resource
    private lateinit var authInterceptor: AuthInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**")
    }
}
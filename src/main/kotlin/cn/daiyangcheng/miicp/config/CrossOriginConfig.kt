package cn.daiyangcheng.miicp.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration

@WebFilter(filterName = "CorsFilter")
@Configuration
class CrossOriginConfig : Filter {
    override fun doFilter(
        req: ServletRequest?,
        res: ServletResponse?,
        chain: FilterChain?
    ) {
        val request: HttpServletRequest? = req as? HttpServletRequest
        val response: HttpServletResponse? = res as? HttpServletResponse

        if (request?.method?.contains("OPTIONS") ?: false) {
            response?.status = HttpServletResponse.SC_OK;
            return;
        }
        response?.setHeader("Access-Control-Allow-Origin", "*")
        response?.setHeader("Access-Control-Allow-Credentials", "true")
        response?.setHeader("Access-Control-Allow-Methods", "*")
        response?.setHeader("Access-Control-Max-Age", "3600")
        response?.setHeader("Access-Control-Allow-Headers", "*")

        chain?.doFilter(req, res)
    }
}
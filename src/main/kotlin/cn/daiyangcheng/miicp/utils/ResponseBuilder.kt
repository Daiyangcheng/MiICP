package cn.daiyangcheng.miicp.utils

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONWriter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseBuilder {
    fun toMap(rs: Any): Map<String, Any?> {
        return JSON.parseObject(JSON.toJSONString(rs, JSONWriter.Feature.WriteMapNullValue))
    }

    /**
     * 成功响应
     * @param message 返回消息
     * @param data 返回数据
     */
    fun success(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        return build(Response.OK, message, data)
    }

    /**
     * 未找到资源响应
     * @param message 返回消息
     * @param data 返回数据
     */
    fun notFound(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        return build(Response.NOT_FOUND, message, data)
    }

    /**
     * 无效请求响应
     * @param message 返回消息
     * @param data 返回数据
     */
    fun badRequest(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        // 修复了原始代码中将 BAD_REQUEST 误设为 TOO_MANY_REQUESTS 的bug
        return build(Response.BAD_REQUEST, message, data)
    }

    /**
     * 请求过多响应
     * @param message 返回消息
     * @param data 返回数据
     * @param retry 多少秒后重试 (通过 Retry-After 头返回)
     */
    fun tooManyRequests(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null,
        retry: Long? = null
    ): ResponseEntity<Map<String, Any?>> {
        val headers = HttpHeaders()
        if (retry != null) {
            headers.add("Retry-After", retry.toString())
        }
        return build(Response.TOO_MANY_REQUESTS, message, data, headers)
    }

    /**
     * 内部服务器错误响应
     * @param message 返回消息
     * @param data 返回数据
     */
    fun exception(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        return build(Response.INTERNAL_SERVER_ERROR, message, data)
    }

    /**
     * 禁止访问响应 (无权限)
     * @param message 返回消息
     * @param data 返回数据
     */
    fun forbidden(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        return build(Response.FORBIDDEN, message, data)
    }

    /**
     * 未授权响应
     * @param message 返回消息
     * @param data 返回数据
     */
    fun unauthorized(
        message: String? = null, // 消息优先
        data: Map<String, Any?>? = null
    ): ResponseEntity<Map<String, Any?>> {
        return build(Response.UNAUTHORIZED, message, data)
    }

    /**
     * 服务不可用响应
     */
    fun serviceUnavailable(): ResponseEntity<Map<String, Any?>> {
        return build(Response.SERVICE_UNAVAILABLE)
    }

    /**
     * 茶壶响应
     */
    fun imATeapot(): ResponseEntity<Map<String, Any?>> {
        return build(Response.IM_A_TEAPOT)
    }

    /**
     * 自定义状态码响应
     * @param status HTTP 状态码
     * @param message 返回消息
     * @param data 返回数据
     * @param headers 自定义 HTTP 头
     */
    fun custom(
        status: HttpStatus,
        message: String? = null,
        data: Map<String, Any?>? = null,
        headers: HttpHeaders? = null
    ): ResponseEntity<Map<String, Any?>> {
        val dataMap = HashMap<String, Any?>()
        dataMap["status"] = status.value()
        dataMap["message"] = message ?: status.name // 如果 message 为 null，使用 HttpStatus 的名称
        dataMap["data"] = data ?: HashMap<String, Any?>() // 如果 data 为 null，使用空 HashMap
        return ResponseEntity.status(status.value())
            .headers(headers)
            .body(dataMap)
    }

    /**
     * 内部构建响应的私有方法
     * @param type 响应类型 (来自 Response 枚举)
     * @param message 返回消息
     * @param data 返回数据
     * @param headers 自定义 HTTP 头
     */
    private fun build(
        type: Response,
        message: String? = null,
        data: Map<String, Any?>? = null,
        headers: HttpHeaders? = null
    ): ResponseEntity<Map<String, Any?>> {
        val dataMap = HashMap<String, Any?>()
        dataMap["status"] = type.code
        dataMap["message"] = message ?: type.message
        dataMap["data"] = data ?: HashMap<String, Any?>()
        return ResponseEntity.status(type.code)
            .headers(headers)
            .body(dataMap)
    }

    /**
     * 定义标准响应类型及其对应的状态码和默认消息
     */
    enum class Response(val code: Int, val message: String) {
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403, "Forbidden"),
        BAD_REQUEST(400, "Bad Request"),
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        IM_A_TEAPOT(418, "I'm a teapot"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    }
}
package cn.daiyangcheng.miicp.aspect

import cn.daiyangcheng.miicp.annotation.Limit
import cn.daiyangcheng.miicp.utils.ResponseBuilder
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Aspect
@Component
class LimitAspect {

    private val requestCounts = ConcurrentHashMap<String, AtomicInteger>()
    private val windowStart = ConcurrentHashMap<String, Long>()
    private val spelParser = SpelExpressionParser()

    @Around("@annotation(limit)")
    fun around(joinPoint: ProceedingJoinPoint, limit: Limit): Any? {
        val key = buildKey(joinPoint, limit)
        val now = System.currentTimeMillis()
        val windowStartTime = windowStart.getOrPut(key) { now }
        
        if (now - windowStartTime > limit.period * 1000) {
            windowStart[key] = now
            requestCounts[key] = AtomicInteger(0)
        }
        
        val count = requestCounts.getOrPut(key) { AtomicInteger(0) }
        if (count.incrementAndGet() > limit.count) {
            val remainingSeconds = limit.period - (now - windowStartTime) / 1000
            val builder = ResponseBuilder()
            return builder.tooManyRequests(
                message = "请求过于频繁，请于${remainingSeconds}秒后再试",
                retry = remainingSeconds
            )
        }
        
        return joinPoint.proceed()
    }

    private fun buildKey(joinPoint: ProceedingJoinPoint, limit: Limit): String {
        val methodName = joinPoint.signature.name
        return if (limit.key.isNotEmpty()) {
            val context = StandardEvaluationContext()
            val args = joinPoint.args
            val parameterNames = joinPoint.signature.declaringType.declaredMethods
                .find { it.name == joinPoint.signature.name }?.parameters?.map { it.name } ?: emptyList()
            
            // 设置参数到SpEL上下文
            args.forEachIndexed { index, arg ->
                if (index < parameterNames.size) {
                    context.setVariable(parameterNames[index], arg)
                }
                context.setVariable("p$index", arg)
            }
            
            val expression = spelParser.parseExpression(limit.key)
            val keyValue = expression.getValue(context)?.toString() ?: ""
            "$methodName:$keyValue"
        } else {
            methodName
        }
    }
}
package cn.daiyangcheng.miicp.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Limit(
    val key: String = "",
    val period: Long = 60,
    val count: Int = 1
)
package cn.daiyangcheng.miicp.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NeedAuthorization(
    val role: String = "ADMIN"
)
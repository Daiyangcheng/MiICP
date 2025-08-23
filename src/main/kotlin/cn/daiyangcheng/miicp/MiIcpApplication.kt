package cn.daiyangcheng.miicp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@EnableScheduling
class MiIcpApplication

fun main(args: Array<String>) {
    runApplication<MiIcpApplication>(*args)
}

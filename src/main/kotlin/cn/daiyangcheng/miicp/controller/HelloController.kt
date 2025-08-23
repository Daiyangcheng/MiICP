package cn.daiyangcheng.miicp.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/")
    fun hello(): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "message" to "Thank you for using MiICP! Made by DYC",
            "status" to "success"
        )
        return ResponseEntity.ok(response)
    }
}
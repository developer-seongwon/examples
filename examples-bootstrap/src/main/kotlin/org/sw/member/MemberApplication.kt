package org.sw.member

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemberDddApplication

fun main(args: Array<String>) {
    runApplication<MemberDddApplication>(*args)
}

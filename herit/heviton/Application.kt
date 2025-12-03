package net.herit.heviton

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	// config 디렉토리의 설정 파일 사용
//	System.setProperty("spring.config.location", "classpath:/,file:./config/")
//	System.setProperty("logging.config", "file:./config/logback-spring.xml")
	
	runApplication<Application>(*args)
}

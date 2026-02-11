plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

springBoot {
    buildInfo()
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

dependencies {
    implementation(project(":examples-domain"))
    implementation(project(":examples-application"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

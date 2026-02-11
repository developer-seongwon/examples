plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":examples-domain"))
    implementation(project(":examples-application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

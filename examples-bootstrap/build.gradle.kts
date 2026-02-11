plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

tasks.bootRun {
    workingDir = rootProject.projectDir
}

dependencies {
    implementation(project(":examples-domain"))
    implementation(project(":examples-application"))
    implementation(project(":examples-infra"))
    implementation(project(":examples-api"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
}

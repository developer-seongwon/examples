plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":examples-domain"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
}

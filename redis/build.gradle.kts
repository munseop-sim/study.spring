plugins {
    id("java")
    idea
    kotlin("jvm") version "2.1.0"
}
val springVersion = findProperty("spring.version")!!.toString()

group = "study.ms2709.redis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-redis:$springVersion")
    implementation(project(":common"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/com.github.codemonstur/embedded-redis
    testImplementation("com.github.codemonstur:embedded-redis:1.4.3")
}

tasks.test {
    useJUnitPlatform()
}

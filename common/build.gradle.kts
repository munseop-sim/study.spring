plugins {
    idea
    kotlin("jvm") version "2.1.0"
}

group = "study.ms2709"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // spring-boot-starter-logging 3.4.1 환경의 로그
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-logging/3.4.1
    val log4jToSlf4j = "2.24.3"
    val logback = "1.5.12"
    val julToSlf4j = "2.0.16"

    api("ch.qos.logback:logback-classic:$logback")
    api("org.apache.logging.log4j:log4j-to-slf4j:$log4jToSlf4j")
    api("org.slf4j:jul-to-slf4j:$julToSlf4j")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

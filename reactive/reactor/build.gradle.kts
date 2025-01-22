plugins {
    kotlin("jvm") version "2.1.0"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

group = "study.ms2709"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    val reactor = "3.7.2"
    implementation("io.projectreactor:reactor-core:$reactor")
    implementation(project(":common"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:2024.0.2")
    }
}

sourceSets {
    main {
        java.srcDir("src/main/kotlin")
    }
}

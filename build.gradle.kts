plugins {
    id("maven-publish")
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

group = "org.iris-api-support"
version = "1.1"


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("io.ktor:ktor-client-core:3.2.3")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("io.ktor:ktor-client-okhttp-jvm:3.2.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.register("hello") {
    doLast {
        println("Hello from Gradle with JDK: ${System.getProperty("java.home")}")
    }
}
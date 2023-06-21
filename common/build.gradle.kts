plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20-Beta"
    id("org.jetbrains.dokka") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kotlinVersion = "1.8.0"
    val koinVersion = "3.3.3"
    val mockkVersion = "1.13.4"

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation(kotlin("serialization", version = kotlinVersion))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("org.slf4j:slf4j-log4j12:2.0.6")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
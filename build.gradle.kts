import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}

group = "me.gabrielfioretti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("script-runtime"))
}

tasks.test {
    useTestNG()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
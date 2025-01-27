plugins {
    kotlin("jvm") version "1.9.0"
}

kotlin {
    jvmToolchain(17)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.seleniumhq.selenium:selenium-java:4.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("successToFile")
    archiveVersion.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        // Указываем точку входа на файл, который генерирует команду
        attributes["Main-Class"] = "org.example.CheckUrlsSeleniumCommandKt"
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

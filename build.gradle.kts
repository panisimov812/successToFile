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


val ajaltVersion = "2.8.0"
val slf4jVersion = "2.0.16"
val seleniumVersion = "4.11.0"
val junitVersion = "5.7.0"

dependencies {
    implementation("com.github.ajalt:clikt:$ajaltVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
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

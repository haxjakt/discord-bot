import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "net.haxjakt.demo.pmatcher"
version = "1.0-SNAPSHOT"

val mainClassName = "net.haxjakt.demo.pmatcher.Main"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.16")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("org.reflections:reflections:0.10.2")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("discord-bot-fat.jar")
    archiveVersion.set("0.0.1")
    manifest {
        attributes["Main-Class"] = "net.haxjakt.bot.BotApplication"
    }
}
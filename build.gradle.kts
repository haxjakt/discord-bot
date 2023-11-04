plugins {
    id("java")
}

group = "net.haxjakt.demo.pmatcher"
version = "1.0-SNAPSHOT"

val mainClassName = "net.haxjakt.demo.pmatcher.Main"

repositories {
    mavenCentral()
}

tasks.jar {
    archiveFileName.set("discord-bot.jar")
    archiveVersion.set("0.0.1")
}

artifacts {
    archives(tasks.jar)
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.16")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
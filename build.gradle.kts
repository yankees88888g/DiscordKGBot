import org.gradle.internal.io.RandomAccessFileInputStream
import java.io.FileInputStream
import java.util.*

plugins {
    id("java")
}

group = "com.yankees88888g"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    var localProps = Properties()
    localProps.load(FileInputStream(rootProject.file("bot.properties")))
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/earthmc-toolkit/earthmc-wrapper")
        credentials {
            username = localProps.getProperty("USERNAME")
            password = localProps.getProperty("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.12")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.github.emcw:emc-wrapper:0.11.2")
}

tasks.test {
    useJUnitPlatform()
}
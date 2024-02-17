import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass = "ru.kpfu.itis.lobanov.Main"
}

group = "ru.kpfu.itis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:${properties["springVersion"]}")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:${properties["tomcatVersion"]}")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<ShadowJar> {
//    archiveFileName.set("hello.jar")
    mergeServiceFiles()
//    manifest {
//        attributes(mapOf("Main-Class" to "${properties["mainClassName"]}"))
////        attributes
//    }
}

tasks.test {
    useJUnitPlatform()
}
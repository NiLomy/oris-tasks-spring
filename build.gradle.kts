import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("war")
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
    implementation("org.springframework:spring-jdbc:${properties["springVersion"]}")
    implementation("org.springframework:spring-orm:${properties["springVersion"]}")
    implementation("org.springframework:spring-context-support:${properties["springVersion"]}")
    implementation("org.springframework.security:spring-security-core:${properties["springSecurityVersion"]}")
    implementation("org.springframework.security:spring-security-web:${properties["springSecurityVersion"]}")
    implementation("org.springframework.security:spring-security-config:${properties["springSecurityVersion"]}")
    implementation("org.springframework.security:spring-security-taglibs:${properties["springSecurityVersion"]}")
    implementation("org.springframework.data:spring-data-jpa:${properties["dataJpaVersion"]}")
    implementation("org.hibernate:hibernate-core:${properties["hibernateVersion"]}")
    implementation("org.hibernate:hibernate-entitymanager:${properties["hibernateVersion"]}")
//    implementation("org.hibernate:hibernate-validator:${properties["hibernateVersion"]}")
    implementation("org.freemarker:freemarker:${properties["freemarkerVersion"]}")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:${properties["tomcatVersion"]}")
    implementation("com.google.code.gson:gson:${properties["gsonVersion"]}")
    implementation("com.mchange:c3p0:${properties["c3p0Version"]}")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    compileOnly("org.projectlombok:lombok:${properties["lombokVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${properties["lombokVersion"]}")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:${properties["hibernateVersion"]}")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation(platform("org.junit:junit-bom:${properties["junitVersion"]}"))
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
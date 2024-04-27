import java.util.Properties

plugins {
    id("java")
//    id("war")
//    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.springframework.boot") version "2.7.17"
    id("application")
    id("org.liquibase.gradle") version "2.2.0"
    id("jacoco")
}

apply(plugin = "io.spring.dependency-management")

application {
    mainClass = "ru.kpfu.itis.lobanov.Main"
}

group = "ru.kpfu.itis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.security:spring-security-taglibs:${properties["springSecurityVersion"]}")
    testImplementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:${properties["hibernateVersion"]}")
    implementation("com.google.code.gson:gson:${properties["gsonVersion"]}")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.apache.tomcat:tomcat-jsp-api:10.1.20")
//    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    implementation("javax.servlet.jsp:jsp-api:2.1")
    compileOnly("org.projectlombok:lombok:${properties["lombokVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${properties["lombokVersion"]}")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")

    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.webjars:jquery:3.6.0")
    implementation("org.webjars:bootstrap:4.6.0")
    implementation("org.webjars:webjars-locator-core:0.46")

    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("org.springframework.boot:spring-boot-devtools:2.6.0")
    implementation("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
}

//tasks.test {
//    useJUnitPlatform()
//}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude("**/exceptions/**",
                    "**/dtos/**",
                    "**/model/**",
                    "**/repositories/**",
                    "**/security/**",
                    "**/Application.class",
                    "**/httpclient/**",
                    "**/hw/**",
                    "**/configs/**")
        }
    }))
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal() // 50% должно быть покрыто
            }
        }
    }
}

var props = Properties()
props.load(file("src/main/resources/liquibase.properties").inputStream())

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to props.get("change-log-file"),
            "url" to props.get("url"),
            "username" to props.get("username"),
            "password" to props.get("password"),
            "driver" to props.get("driver-class-name"),
        )
    }
}

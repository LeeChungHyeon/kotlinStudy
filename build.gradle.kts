plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

extra["ioCloudVersion"] = "2.4.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:${property("ioCloudVersion")}")
    }
}

dependencies {

    implementation("com.github.okihouse:autocomplete:1.0.2")

    implementation("com.auth0:java-jwt:3.19.2")

    // https://mvnrepository.com/artifact/net.jodah/expiringmap
    implementation("net.jodah:expiringmap:0.5.10")
    implementation("it.ozimov:embedded-redis:0.7.3") { exclude(group = "org.slf4j", module = "slf4j-simple") }

    implementation("io.awspring.cloud:spring-cloud-starter-aws")
    implementation("io.findify:s3mock_2.13:0.2.6")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // https://mvnrepository.com/artifact/io.github.serpro69/kotlin-faker
    implementation("io.github.serpro69:kotlin-faker:1.15.0")
    // https://mvnrepository.com/artifact/com.github.gavlyukovskiy/p6spy-spring-boot-starter
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")
    // https://mvnrepository.com/artifact/io.github.microutils/kotlin-logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")

    // https://mvnrepository.com/artifact/com.linecorp.kotlin-jdsl/spring-data-kotlin-jdsl-starter
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:2.2.1.RELEASE")

    implementation("ch.qos.logback:logback-classic:1.5.16")
    implementation("ch.qos.logback:logback-core:1.5.16")
    implementation("org.codehaus.janino:janino:3.1.10")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.7.0")

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframewordk.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    //testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    //testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}

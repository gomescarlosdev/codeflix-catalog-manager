plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "br.com.gomescarlosdev.codeflix.catalog-infrastructure"
version = "0.0.1"

tasks.bootJar {
    archiveFileName = "codeflix-catalog-app-${version}.jar"
    destinationDirectory.set(file("${rootProject.buildDir}/libs"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codeflix-catalog-domain"))
    implementation(project(":codeflix-catalog-application"))

    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("com.h2database:h2")
}

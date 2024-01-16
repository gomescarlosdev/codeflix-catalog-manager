buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-mysql:9.22.3")
    }
}

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.flywaydb.flyway") version "9.22.3"
}

group = "br.com.gomescarlosdev.codeflix.catalog-infrastructure"
version = "0.0.1"
val swaggerVersion = "2.3.0"

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

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb:flyway-core")
    testRuntimeOnly("com.h2database:h2")
}

flyway {
    url = System.getenv("FLYWAY_DB") ?: "jdbc:mysql://localhost:3306/codeflix_catalog_db"
    user = System.getenv("FLYWAY_USER") ?: "root"
    password = System.getenv("FLYWAY_PASS") ?: "codeflix_catalog_db"
}


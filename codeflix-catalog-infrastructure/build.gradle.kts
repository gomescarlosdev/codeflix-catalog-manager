plugins {
    id("java")
}

group = "br.com.gomescarlosdev.codeflix-catalog-infrastructure"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codeflix-catalog-domain"))
    implementation(project(":codeflix-catalog-application"))
}

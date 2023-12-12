plugins {
    id("java")
}

group = "br.com.gomescarlosdev.codeflix-catalog-application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codeflix-catalog-domain"))
}

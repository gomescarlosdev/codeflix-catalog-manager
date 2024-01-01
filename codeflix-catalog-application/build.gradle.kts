plugins {
    id("java")
}

group = "br.com.gomescarlosdev.codeflix.catalog-application"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":codeflix-catalog-domain"))
}

plugins {
    id("java")
}

group = "br.com.gomescarlosdev.codeflix-cm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin("java")
    }

    dependencies {
        implementation("io.vavr:vavr:0.10.4")

        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor ("org.projectlombok:lombok:1.18.30")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.awaitility:awaitility:4.1.1")
        testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")

        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

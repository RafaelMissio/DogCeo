plugins {
    id("java")
    id("io.qameta.allure") version "2.12.0"
}

group = "br.com.missio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("commons-codec:commons-codec:1.15")

    // RestAssured
    testImplementation("io.rest-assured:rest-assured:6.0.0") // se ainda não estiver

    // https://mvnrepository.com/artifact/tools.jackson.core/jackson-databind
    implementation("tools.jackson.core:jackson-databind:3.0.3") // Jackson

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")

    // Allure + JUnit 5
    testImplementation("io.qameta.allure:allure-junit5:2.32.0")
}

configurations.all {
    resolutionStrategy {
        force("commons-codec:commons-codec:1.15")
    }
}

// Ensure Java compilation uses UTF-8 encoding so source files and messages keep accents
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}


tasks.test {
    useJUnitPlatform()

    // Force UTF-8 for tests and for any tools (Allure) that read test output
    systemProperty("file.encoding", "UTF-8")
    jvmArgs = listOf("-Dfile.encoding=UTF-8", "-Duser.language=pt", "-Duser.country=BR")

    testLogging {
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
        showStandardStreams = true
    }
}

allure {
    version.set("2.32.0")
    adapter {
        autoconfigure.set(true)
        aspectjWeaver.set(true)
        frameworks {
            junit5 {
                adapterVersion.set("2.32.0")
            }
        }
    }
}

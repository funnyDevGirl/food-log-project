import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	java
	application
	id("io.freefair.lombok") version "8.6"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("checkstyle")
	id("jacoco")
	id("com.github.ben-manes.versions") version "0.48.0"

}

group = "ru.foodlog"
version = "0.0.1-SNAPSHOT"


application {
	mainClass.set("ru.foodlog.Application")
}


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

checkstyle {
	configFile = file("config/checkstyle/checkstyle.xml")
	toolVersion = "10.13.0"
}

repositories {
	mavenCentral()
}

object Versions {
	const val MAPSTRUCT_BINDING = "0.2.0"
	const val MAPSTRUCT = "1.5.5.Final"
	const val POSTGRESQL = "42.7.5"
	const val TESTCONTAINERS = "1.20.5"
	const val TESTCONTAINERS_JUNIT = "1.20.5"
	const val SPRING = "3.4.3"
	const val SECURITY_TEST = "6.4.3"
	const val JUNIT_PLATFORM_LAUNCHER = "1.12.0"
	const val JUNIT = "5.12.0"
}

dependencies {

	// MapStruct
	implementation("org.mapstruct:mapstruct:${Versions.MAPSTRUCT}")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${Versions.MAPSTRUCT_BINDING}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")

	// Spring Starters
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Versions.SPRING}")
	implementation("org.springframework.boot:spring-boot-starter-security:${Versions.SPRING}")
	implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING}")
	implementation("org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING}")

	// Testing
	testImplementation(platform("org.junit:junit-bom:${Versions.JUNIT}"))
	testImplementation("org.springframework.boot:spring-boot-starter-test:${Versions.SPRING}")
	testImplementation("org.springframework.security:spring-security-test:${Versions.SECURITY_TEST}")

	// Database
	runtimeOnly("org.postgresql:postgresql:${Versions.POSTGRESQL}")

	// Testcontainers
	testImplementation("org.testcontainers:testcontainers:${Versions.TESTCONTAINERS}")
	testImplementation("org.testcontainers:junit-jupiter:${Versions.TESTCONTAINERS_JUNIT}")
	testImplementation("org.testcontainers:postgresql:${Versions.TESTCONTAINERS}")


}

tasks.withType<Test> {
	finalizedBy(tasks.jacocoTestReport)
	useJUnitPlatform()
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStandardStreams = true
	}
}

tasks.checkstyleMain {
	reports {
		xml.required.set(false)
		html.required.set(true)
	}
}

tasks.checkstyleTest {
	reports {
		xml.required.set(false)
		html.required.set(true)
	}
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
	}
}

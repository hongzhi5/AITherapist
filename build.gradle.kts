plugins {
    java
    checkstyle
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.18.0"
    id("org.flywaydb.flyway") version "7.15.0"
}

group = "com.abx"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.18.0")
    implementation ("org.springframework.kafka:spring-kafka")

    annotationProcessor("org.immutables:value:2.9.3")
    compileOnly("org.immutables:value:2.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    runtimeOnly("org.postgresql:postgresql") // postgres driver

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Checkstyle>("checkstyleMain").configure {
    source = fileTree("src/main/java")
}
tasks.named<Checkstyle>("checkstyleTest").configure {
    source = fileTree("src/test/java")
}

val checkstylePublicTask = tasks.register("checkstyle") {
    group = "verification"
    description = "Runs all Checkstyle checks."
}

tasks.withType<Checkstyle>().forEach { checkstyleTask ->
    checkstylePublicTask { dependsOn(checkstyleTask) }
}

spotless {
    isEnforceCheck = false
    java {
        palantirJavaFormat("2.28.0")
        targetExclude("**/build/generated/**")
    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/hollywood_therapist_db"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("public")
}
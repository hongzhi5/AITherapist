plugins {
    java
    checkstyle
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.diffplug.spotless") version "6.13.0"
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
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.18.0")
    implementation ("org.springframework.kafka:spring-kafka")

    annotationProcessor("org.immutables:value:2.9.3")
    compileOnly("org.immutables:value:2.9.3")
    implementation("org.immutables:value:2.9.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.15.4")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.15.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")


    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")

    implementation ("com.theokanning.openai-gpt3-java:service:0.18.2")
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

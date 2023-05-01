plugins {
    id("java")
}

group = "com.automator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(files("libs/jnativehook-2.2.2.jar"))
}

tasks.test {
    useJUnitPlatform()
}
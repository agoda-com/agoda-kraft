plugins {
    kotlin("jvm") version "1.8.20"
    `java-library`
    `maven-publish`
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "io.agodadev"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.pinterest.ktlint:ktlint-core:0.48.2")
    implementation("io.gitlab.arturbosch.detekt:detekt-api:1.22.0")
    
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.22.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("kraft-ktlint") {
            artifactId = "kraft-ktlint"
            from(components["java"])
        }
        create<MavenPublication>("kraft-detekt") {
            artifactId = "kraft-detekt"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/agoda-com/agoda-kraft")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
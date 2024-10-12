plugins {
    kotlin("jvm") version "1.8.20"
    `java-library`
    `maven-publish`
    `signing`
}

group = "io.agodadev"
// version will be taken from build parameters

description = "Agoda's opinionated Kotlin code quality tools based on experience at scale"

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))  // To be compatible with lib consumers
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri(layout.buildDirectory.dir("staging-deploy").get().asFile.toString())
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.pinterest.ktlint:ktlint-core:0.48.2")
    implementation("io.gitlab.arturbosch.detekt:detekt-api:1.23.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.0")
    testImplementation("org.mockito:mockito-core:4.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("agoda-kraft")
}

tasks.named<Jar>("javadocJar") {
    archiveBaseName.set("agoda-kraft")
}

tasks.named<Jar>("sourcesJar") {
    archiveBaseName.set("agoda-kraft")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "agoda-kraft"
            from(components["java"])
            pom {
                name.set("agoda-kraft")
                description.set(project.description)
                url.set("https://github.com/agoda-com/agoda-kraft")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("agoda")
                        name.set("Agoda")
                        email.set("opensource@agoda.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/agoda-com/agoda-kraft.git")
                    developerConnection.set("scm:git:ssh://github.com/agoda-com/agoda-kraft.git")
                    url.set("https://github.com/agoda-com/agoda-kraft/tree/main")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy").get().asFile.toString())
        }
    }
}

import java.io.ByteArrayOutputStream

plugins {
  `java-library`
  checkstyle
  `maven-publish`
  id("com.github.johnrengelman.shadow") version "5.2.0"
}

val artifactoryUser = (project.findProperty("username")
    ?: System.getenv("ARTIFACTORY_USER")) as String?
val artifactoryPassword = (project.findProperty("password")
    ?: System.getenv("ARTIFACTORY_PASSWORD")) as String?

group = "de.eaglefamily.minecraft"
version = version()

repositories {
  jcenter()
  maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
  implementation("com.google.guava:guava:29.0-jre")
  implementation("com.google.inject:guice:4.2.3")
  compileOnly("com.destroystokyo.paper:paper-api:1.16.1-R0.1-SNAPSHOT")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
  withJavadocJar()
  withSourcesJar()
}

checkstyle {
  toolVersion = "8.35"
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = project.group.toString()
      artifactId = project.name
      version = project.version.toString()

      from(components["java"])
      artifact(tasks["shadowJar"])
    }
  }
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/BlackEagleEF/" + rootProject.name)
      credentials {
        username = artifactoryUser
        password = artifactoryPassword
      }
    }
    mavenLocal {}
  }
}

val tokens = mapOf(
    "NAME" to rootProject.name,
    "PACKAGE_NAME" to project.name.replace("-", "."),
    "GROUP" to project.group,
    "VERSION" to project.version)
tasks.withType<Jar> {
  filesMatching("*.yml") {
    filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokens)
  }
}


fun version(): String {
  val refName = System.getenv("GITHUB_REF")
  if (refName != null && refName.startsWith("refs/heads/")
      && refName != "refs/heads/master" && refName != "refs/heads/dev") {
    return refName.removePrefix("refs/heads/").replace("/", "-")
  }

  val gitTag = extractGitTag()
  return (if (refName == null) "$gitTag-dev" else gitTag).removePrefix("v")
}

fun extractGitTag(): String {
  val out = ByteArrayOutputStream()
  exec {
    isIgnoreExitValue = true
    setCommandLine("git", "describe", "--tags", "--always", "--first-parent")
    standardOutput = out
  }

  return if (out.size() == 0) "unknown" else out.toString("UTF-8").trim()
}

defaultTasks("clean", "check", "build", "publishToMavenLocal")

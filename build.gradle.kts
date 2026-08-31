plugins {
    id("net.fabricmc.fabric-loom") version "1.17.2"
    `maven-publish`
}

group = "io.github.mzuber"
version = "0.1.1"

base {
    archivesName.set("shared-villager-discounts")
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    compileOnly("com.terraformersmc:modmenu:${property("modmenu_version")}")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version" to project.property("loader_version")
    )

    inputs.properties(props)
    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

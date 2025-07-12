plugins {
    `maven-publish`
    id("fabric-loom")
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

version = "${property("mod.version")}+${stonecutter.current.version}"
group = property("mod.group") as String
base.archivesName = property("mod.id") as String

repositories {
    /**
     * Restricts dependency search of the given [groups] to the [maven URL][url],
     * improving the setup speed.
     */
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://maven.quiltmc.org/repository/release/", "Quilt", "org.quiltmc")
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")

    maven {
        name = "Xander"
        url = uri("https://maven.isxander.dev/releases")
    }
}

dependencies {
    /**
     * Fetches only the required Fabric API modules to not waste time downloading all of them for each version.
     * @see <a href="https://github.com/FabricMC/fabric">List of Fabric API modules</a>
     */
    fun fapi(vararg modules: String) {
        for (it in modules) modImplementation(fabricApi.module(it, property("deps.fabric_api") as String))
    }

    minecraft("com.mojang:minecraft:${stonecutter.current.version}")
    mappings("net.fabricmc:yarn:${property("deps.yarn")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

    if (stonecutter.eval(stonecutter.current.version, "1.19.4")) {
        modImplementation("maven.modrinth:1eAoo2KR:${property("deps.yacl")}")
    } else {
        modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    }

    modCompileOnly("maven.modrinth:mOgUt4GM:${property("deps.modmenu")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
}

loom {
    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true") // Exports transformed classes for debugging
        runDir = "../../run" // Shares the run directory between versions
    }
}

java {
    withSourcesJar()
    val requiresJava21: Boolean = stonecutter.eval(stonecutter.current.version, ">=1.20.6")
    val javaVersion: JavaVersion =
        if (requiresJava21) JavaVersion.VERSION_21
        else JavaVersion.VERSION_17
    targetCompatibility = javaVersion
    sourceCompatibility = javaVersion
}

tasks {
    processResources {
        inputs.property("id", project.property("mod.id"))
        inputs.property("name", project.property("mod.name"))
        inputs.property("version", project.property("mod.version"))
        inputs.property("minecraft", project.property("mod.mc_dep"))

        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep")
        )

        filesMatching("fabric.mod.json") { expand(props) }
    }

    // Builds the version into a shared folder in `build/libs/${mod version}/`
    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}


publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${project.property("mod.name")} ${project.property("mod.version")}+${stonecutter.current.version}"
    version = "${project.property("mod.version")}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    dryRun = providers.environmentVariable("MODRINTH_TOKEN")
        .getOrNull() == null

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(stonecutter.current.version)
        requires {
            slug = "fabric-api"
        }
    }

    /*github {
        repository = "https://github.com/BigBrainRobin29/DurabilityGuard"
        commitish = "main"
    }*/
}

/*
publishing {
    repositories {
        maven("...") {
            name = "..."
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${property("mod.group")}.${mod.id}"
            artifactId = mod.version
            version = mcVersion

            from(components["java"])
        }
    }
}
*/
#!/usr/bin/env kotlin
/*
Forgeのバージョン確認Jsonを更新
*/

@file:DependsOn("com.google.code.gson:gson:2.10.1")

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

val rawVersion = args[0]
val rawChangeLog = args[1]
val repo = args[2]

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

val vcName = "version_check.json"
val wrkDir: Path = System.getenv("GITHUB_WORKSPACE")?.let(Path::of) ?: Paths.get("./")
val vcFile: File = wrkDir.resolve(vcName).toFile()

val gp: Map<String, String> = wrkDir.resolve("gradle.properties")
        .let { Files.lines(it) }
        .filter { !it.trim().startsWith("#") }
        .map { it.split("=") }
        .collect(Collectors.toMap({ it[0].trim() }, { it[1].trim() }))

val version = rawVersion.substring(1)
val defaultHomepage = "https://github.com/$repo/releases"
var mcVersions = setOf(gp["minecraft_version"] ?: gp["mc_version"])
gp["support_versions"]?.let { str -> mcVersions += str.split(",").map { it.trim() } }

val changeLog = rawChangeLog.let {
    val line = it.lines().filter { tis -> tis.isNotEmpty() }
    var ret = ""
    for (i in line.indices) {
        val lie = line[i]
        if (lie.startsWith("### ") && (i == line.size - 1 || line[i + 1].startsWith("### ")))
            continue

        ret += if (lie.startsWith("### ")) {
            lie.substring("### ".length) + "\n"
        } else {
            lie + "\n"
        }
    }
    return@let ret
}

val recommended = gp["release_type"]?.equals("release", true) ?: true

var jo: JsonObject

if (vcFile.exists()) {
    FileReader(vcFile, StandardCharsets.UTF_8).use { r -> BufferedReader(r).use { jo = gson.fromJson(it, JsonObject::class.java) } }
} else {
    jo = JsonObject()
    jo.addProperty("homepage", defaultHomepage)
}

val promosJo = jo.getAsJsonObject("promos") ?: JsonObject()

for (mcVersion in mcVersions) {
    val vEntry = jo.getAsJsonObject(mcVersion) ?: JsonObject()
    vEntry.addProperty(version, changeLog)
    jo.add(mcVersion, vEntry)

    promosJo.addProperty("$mcVersion-latest", version)
    if (recommended || !promosJo.has("$mcVersion-recommended"))
        promosJo.addProperty("$mcVersion-recommended", version)
}

jo.add("promos", promosJo)

FileWriter(vcFile, StandardCharsets.UTF_8).use { w -> BufferedWriter(w).use { gson.toJson(jo, it) } }
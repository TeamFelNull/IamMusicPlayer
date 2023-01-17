#!/usr/bin/env kotlin
/*
プロジェクトの検証
*/

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

val tag = args[0]
val changeLog = args[1]
val version = tag.substring(1)

if (changeLog.lines().filter { it.trim().isNotEmpty() }.none { !it.trim().startsWith("### ") && it.trim().startsWith("- ") })
    throw Exception("Unwritten change log/更新ログが未記述です")

fun assertFile(pathStr: String) {
    if (!File(pathStr).exists())
        throw Exception("Required file does not exist/必要なファイルが存在しません: $pathStr")
}

assertFile("CHANGELOG.md")
assertFile("README.md")
assertFile("LICENSE")

val wrkDir: Path = System.getenv("GITHUB_WORKSPACE")?.let(Path::of) ?: Paths.get("./")
val gp: Map<String, String> = wrkDir.resolve("gradle.properties")
        .let { Files.lines(it) }
        .filter { !it.trim().startsWith("#") }
        .map { it.split("=") }
        .collect(Collectors.toMap({ it[0].trim() }, { it[1].trim() }))

fun assertGradleProperties(name: String, orName: String) {
    if (gp[name] == null)
        throw Exception("Required value does not exist in gradle.properties/gradle.propertiesに必要な値が存在しません: $name,$orName")
}

fun assertGradleProperties(name: String) {
    if (gp[name] == null)
        throw Exception("Required value does not exist in gradle.properties/gradle.propertiesに必要な値が存在しません: $name")
}

assertGradleProperties("minecraft_version", "mc_version")
assertGradleProperties("archives_base_name", "mod_id")
assertGradleProperties("mod_display_name", "mod_name")
assertGradleProperties("version", "mod_version")
assertGradleProperties("release_type")

var modVersion = gp["version"] ?: gp["mod_version"]

if (version != modVersion)
    throw Exception("gradle.properties version and tag version do not match/gradle.propertiesのバージョンとタグのバージョンが一致しません: $version")

var releaseType = gp["release_type"]

if (!listOf("release", "beta", "alpha").contains(releaseType))
    throw Exception("Release type not valid/リリースタイプが有効ではありません: $releaseType")

println("Project verification completed!")
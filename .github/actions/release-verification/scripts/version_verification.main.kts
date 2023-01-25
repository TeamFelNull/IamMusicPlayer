#!/usr/bin/env kotlin
/*
バージョンの検証
*/

@file:DependsOn("com.vdurmont:semver4j:3.1.0")

import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.SemverException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

val tag = args[0]
val defaultBranch = args[1]
val branches = args[2]
val allTags = args[3]

fun getBranchName(branchName: String): String {
    val strs = branchName.split("/").drop(2)
    return strs.joinToString("/")
}

val wrkDir: Path = System.getenv("GITHUB_WORKSPACE")?.let(Path::of) ?: Paths.get("./")
val gp: Map<String, String> = wrkDir.resolve("gradle.properties")
        .let { Files.lines(it) }
        .filter { it.isNotBlank() }
        .filter { !it.trim().startsWith("#") }
        .map { it.split("=") }
        .collect(Collectors.toMap({ it[0].trim() }, { it[1].trim() }))

val semVerPatten: Pattern = Pattern.compile("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$");

val version = tag.substring(1)
val mainVersion = branches.lines().map { getBranchName(it) }.contains(defaultBranch)
val releaseType = (gp["release_type"] ?: "unknown").lowercase()
val allVersions = allTags.lines().filter { it != tag }.map { it.substring(1) }.filter { semVerPatten.matcher(it).matches() }

println("Version: $version")
println("Main Version: $mainVersion")
println("Release Type: $releaseType")
println("All Versions: $allVersions")
println()

val stabilityOrder = listOf("release", "beta", "alpha")

if (!semVerPatten.matcher(version).matches())
    throw Exception("Does not match SemVer/SemVerに一致しません")

val semVer = Semver(version)
val allSemVer = allVersions.map {
    try {

        val sem = Semver(it)
        if (sem.suffixTokens.isNotEmpty()) {
            if (sem.suffixTokens.size != 2 || sem.suffixTokens[0].all { r -> r.isDigit() } || sem.suffixTokens[1].any { r -> !r.isDigit() })
                return@map null;
        }
        return@map sem
    } catch (e: SemverException) {
        return@map null;
    }
}.filterNotNull()
val verSuffixes: Array<String> = semVer.suffixTokens;

fun toVersionOnly(targetSemVer: Semver): String {
    return "${targetSemVer.major}.${targetSemVer.minor}.${targetSemVer.patch}"
}

if (verSuffixes.isEmpty()) {
    if (!"release".equals(releaseType, true))
        throw Exception("Suffix must be present if not release/リリースではない場合はサフィックスが存在する必要があります")
} else {
    if ("pre".equals(verSuffixes[0], true))
        throw Exception("Pre version cannot be released/申し訳ないがpreバージョンはNG")

    if ("release".equals(releaseType, true))
        throw Exception("Must not suffix if release/リリースの場合はサフィックスが存在してはいけません")

    if (!releaseType.equals(verSuffixes[0], true))
        throw Exception("Release type and suffix mismatch/リリースタイプとサフィックスが一致しません")

    if (verSuffixes.size != 2 || verSuffixes[0].all { it.isDigit() } || verSuffixes[1].any { !it.isDigit() })
        throw Exception("Suffix should be releaseType.number/サフィックスはreleaseType.numberにする必要があります")

    allSemVer.forEach {
        if (toVersionOnly(it) != toVersionOnly(semVer))
            return@forEach

        var suffixesReleaseType = "release"
        if (it.suffixTokens.isNotEmpty() && stabilityOrder.contains(it.suffixTokens[0]))
            suffixesReleaseType = it.suffixTokens[0]

        val order = stabilityOrder.indexOf(verSuffixes[0])
        val itOrder = stabilityOrder.indexOf(suffixesReleaseType)

        if (itOrder < order)
            throw Exception("Already stable version exists/さらに安定しているバージョンが存在します: $it")

        if (suffixesReleaseType == verSuffixes[0]) {
            var suffixesNumber = 0
            if (it.suffixTokens.size == 2 && it.suffixTokens[1].all { r -> r.isDigit() })
                suffixesNumber = it.suffixTokens[1].toInt()

            if (suffixesNumber >= verSuffixes[1].toInt())
                throw Exception("Newer suffix number versions exist/さらに新しいサフィックス番号のバージョンが存在します: $it")
        }
    }

    if (verSuffixes[1].toInt() >= 2) {
        val need = toVersionOnly(semVer) + "-" + verSuffixes[0] + "." + (verSuffixes[1].toInt() - 1);
        if (!allVersions.contains(need))
            throw Exception("Previous suffix version number does not exist/以前のサフィックスのバージョン番号が存在しません: $need")
    }
}


if (allSemVer.isNotEmpty()) {
    var compVer: List<Semver> = ArrayList(allSemVer)

    if (mainVersion) {
        val lastVer = allSemVer.maxWith { a, b -> a.compareTo(b) }
        if (lastVer.isGreaterThan(semVer))
            throw Exception("New version exists/さらに新しいバージョンが存在します: $lastVer")
    } else {
        val predictNextVer = Semver("${semVer.major}.${semVer.minor}.${semVer.patch + 1}-${stabilityOrder.last()}.1")
        compVer = allSemVer.filter { it.isLowerThan(predictNextVer) }
    }

    val preDictPreVer = semVer.let {
        if (it.patch > 0) {
            return@let Semver("${it.major}.${it.minor}.${it.patch - 1}")
        } else {
            if (it.minor > 0) {
                return@let Semver("${it.major}.${it.minor - 1}.0")
            } else {
                if (it.major <= 0)
                    throw Exception("0.0.0!?")
                return@let Semver("${it.major - 1}.0.0")
            }
        }
    }

    val compVo = compVer.map { Semver(toVersionOnly(it)) }.distinct()

    if (compVo.any { it.isLowerThan(preDictPreVer) } && compVo.none { it.isGreaterThanOrEqualTo(preDictPreVer) && it.isLowerThan(version) })
        throw Exception("Pre version does not exist/以前のバージョンが存在しません: $preDictPreVer")

} else {
    println("First version")
}

println("Version verification completed!")
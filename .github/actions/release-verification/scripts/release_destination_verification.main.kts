#!/usr/bin/env kotlin
/*
リリース先の検証
*/

@file:DependsOn("com.google.code.gson:gson:2.10.1")
@file:DependsOn("org.apache.commons:commons-lang3:3.12.0")

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.lang3.concurrent.BasicThreadFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.stream.Collectors

val rep = args[0]
val modrinthApiToken: String? = System.getenv("modrinthtoken")
val curseforgeToken: String? = System.getenv("curseforgetoken")
val mavenPutPass: String? = System.getenv("mavenpassword")

val gson = Gson()
val executor: ExecutorService = Executors.newCachedThreadPool(BasicThreadFactory.Builder().namingPattern("verification-thread-%d").daemon(true).build());

val wrkDir: Path = System.getenv("GITHUB_WORKSPACE")?.let(Path::of) ?: Paths.get("./")
val gp: Map<String, String> = wrkDir.resolve("gradle.properties")
        .let { Files.lines(it) }
        .filter { !it.trim().startsWith("#") }
        .map { it.split("=") }
        .collect(Collectors.toMap({ it[0].trim() }, { it[1].trim() }))

fun toCheckStr(str: String): String {
    var ret = str.lowercase()
    ret = ret.replace("-", "")
    ret = ret.replace("_", "")
    ret = ret.replace(" ", "")
    ret = ret.replace(",", "")
    ret = ret.replace("'", "")
    return ret
}

fun curesforgeCheck(projectId: String) {
    if (curseforgeToken.isNullOrEmpty())
        throw Exception("CuresForge Token is empty/CuresForgeのTokenが空です")

    val url = "https://api.felnull.dev/curseforge-wrapper/mods/$projectId"

    val jo: JsonObject
    try {
        val client = HttpClient.newHttpClient()
        val req = HttpRequest.newBuilder(URI(url)).GET().build()
        val res = client.send(req, HttpResponse.BodyHandlers.ofInputStream())

        jo = res.body().use {
            InputStreamReader(it).use { r ->
                BufferedReader(r).use { n ->
                    gson.fromJson(n, JsonObject::class.java)
                }
            }
        }
    } catch (e: Exception) {
        throw Exception("Failed to check with CuresForgeAPI/CuresForgeAPIに確認できませんでした", e)
    }

    val name = jo.get("name").asString
    val slug = jo.get("slug").asString

    val modName = gp["curesforge_name"] ?: gp["mod_display_name"] ?: gp["mod_name"]
    val chkName = modName?.let { toCheckStr(it) }

    if (toCheckStr(name) != chkName && toCheckStr(slug) != chkName)
        throw Exception("CuresForge project mod name do not match/CuresForgeのプロジェクトのMOD名が一致しません")

    println("CuresForge Ok: $name")
}

fun modrinthCheck(projectId: String) {
    if (modrinthApiToken.isNullOrEmpty())
        throw Exception("ModrinthAPI Token is empty/ModrinthAPIのTokenが空です")

    val url = "https://api.modrinth.com/v2/project/$projectId"
    val jo: JsonObject
    try {
        val uaContact = if (rep.split("/")[0] == "TeamFelnull") {
            "(teamfelnull@felnull.dev)"
        } else {
            "(https://github.com/$rep)"
        }

        val client = HttpClient.newHttpClient()
        val req = HttpRequest.newBuilder(URI(url)).GET().header("user-agent", "$rep/release-verification $uaContact").build()
        val res = client.send(req, HttpResponse.BodyHandlers.ofInputStream())

        jo = res.body().use {
            InputStreamReader(it).use { r ->
                BufferedReader(r).use { n ->
                    gson.fromJson(n, JsonObject::class.java)
                }
            }
        }
    } catch (e: Exception) {
        throw Exception("Failed to check with ModrinthAPI/ModrinthAPIに確認できませんでした", e)
    }

    val title = jo.get("title").asString
    val slug = jo.get("slug").asString

    val modName = gp["modrinth_name"] ?: gp["mod_display_name"] ?: gp["mod_name"]
    val chkName = modName?.let { toCheckStr(it) }

    if (toCheckStr(title) != chkName && toCheckStr(slug) != chkName)
        throw Exception("Modrinth title and project mod name do not match/ModrinthのタイトルとプロジェクトのMOD名が一致しません")

    println("Modrinth Ok: $title")
}

fun mavenCheck(mavenUrl: String) {
    if (mavenPutPass.isNullOrEmpty())
        throw Exception("Maven Password is empty/Mavenのパスワードが空です")

    val ret: Int
    try {
        val client = HttpClient.newHttpClient()
        val req = HttpRequest.newBuilder(URI(mavenUrl)).GET().build()
        val res = client.send(req, HttpResponse.BodyHandlers.ofInputStream())
        ret = res.statusCode()
    } catch (e: Exception) {
        throw Exception("Failed to check with Maven/Mavenに確認できませんでした", e)
    }

    if (ret != 200)
        throw Exception("Maven status code error/Mavenのステータスコードエラー: $ret")

    println("Maven Ok: $mavenUrl")
}

val tasks = mutableListOf<CompletableFuture<Void>>()

gp["curesforge_id"]?.let {
    tasks.add(CompletableFuture.runAsync({ curesforgeCheck(it) }, executor))
}

gp["modrinth_id"]?.let {
    tasks.add(CompletableFuture.runAsync({ modrinthCheck(it) }, executor))
}

gp["maven_put_url"]?.let {
    tasks.add(CompletableFuture.runAsync({ mavenCheck(it) }, executor))
}

val cf: CompletableFuture<Void> = CompletableFuture.allOf(*tasks.toTypedArray())
cf.join()

println()
println("Release Destination verification completed!")
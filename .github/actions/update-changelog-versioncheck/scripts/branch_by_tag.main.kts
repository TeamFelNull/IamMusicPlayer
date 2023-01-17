#!/usr/bin/env kotlin
/*
タグがつけられたブランチを特定する
 */

val defaultBranch = args[0]
val branches = args[1].trimIndent().lines()

fun getBranchName(branchName: String): String {
    val strs = branchName.split("/").drop(2)
    return strs.joinToString("/")
}

fun getBranchByTagBranches(defaultBranch: String, tagBranches: List<String>): String {
    if (tagBranches.size == 1)
        return tagBranches[0]

    if (tagBranches.contains(defaultBranch))
        return defaultBranch

    throw Exception("Could not determine branch")
}

println(getBranchByTagBranches(defaultBranch, branches.map { getBranchName(it) }))
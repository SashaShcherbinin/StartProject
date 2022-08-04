@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.start.data.extention

fun readString(filepath: String): String {
    return object {}.javaClass.getResource("/$filepath").readText()
}

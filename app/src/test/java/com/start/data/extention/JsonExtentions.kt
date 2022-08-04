package com.start.data.extention

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.start.data.di.module.NetworkModule

inline fun <reified T> getJsonByResource(path: String): T {
    val factory = object : TypeToken<T>() {}.type
    return NetworkModule().getGson().fromJson(readJsonElementFromFile(path), factory)
}

fun readJsonElementFromFile(filepath: String): JsonElement {
    val readText = readString(filepath)
    return Gson().fromJson(readText, JsonElement::class.java)
}


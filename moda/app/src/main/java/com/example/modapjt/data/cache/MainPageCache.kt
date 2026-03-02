package com.example.modapjt.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.example.modapjt.data.dto.response.SearchResponse
import com.google.gson.Gson

object MainPageCache {
    private const val PREFS_NAME = "main_page_cache"
    private const val KEY_DATA = "cached_data"
    private const val KEY_VERSION = "cached_version"
    private const val KEY_TIMESTAMP = "cached_timestamp"
    private const val TTL_MS = 24 * 60 * 60 * 1000L // 24시간

    private val gson = Gson()

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun save(context: Context, data: SearchResponse, version: String) {
        prefs(context).edit()
            .putString(KEY_DATA, gson.toJson(data))
            .putString(KEY_VERSION, version)
            .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun load(context: Context): SearchResponse? {
        val json = prefs(context).getString(KEY_DATA, null) ?: return null
        return try {
            gson.fromJson(json, SearchResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getVersion(context: Context): String? {
        return prefs(context).getString(KEY_VERSION, null)
    }

    fun isValid(context: Context): Boolean {
        val timestamp = prefs(context).getLong(KEY_TIMESTAMP, 0)
        if (timestamp == 0L) return false
        return System.currentTimeMillis() - timestamp < TTL_MS
    }

    fun invalidate(context: Context) {
        prefs(context).edit().clear().apply()
    }
}

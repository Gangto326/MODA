package com.example.modapjt.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("search_keywords")

object SearchKeywordDataStore {
    private val KEYWORDS_KEY = stringPreferencesKey("keywords")

    // 검색어 리스트 저장
    suspend fun saveKeywords(context: Context, keywords: List<String>) {
        val keywordsString = keywords.joinToString(",") // 쉼표로 구분된 문자열로 저장
        context.dataStore.edit { preferences ->
            preferences[KEYWORDS_KEY] = keywordsString
        }
    }

    // 검색어 리스트 불러오기
    fun getKeywords(context: Context): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[KEYWORDS_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }
}

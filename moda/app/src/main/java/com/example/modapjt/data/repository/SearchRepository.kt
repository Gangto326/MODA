package com.example.modapjt.data.repository

import android.content.Context
import android.util.Log
import com.example.modapjt.data.api.SearchApiService
import com.example.modapjt.data.cache.MainPageCache
import com.example.modapjt.data.dto.response.HomeKeywordResponse
import com.example.modapjt.data.dto.response.HotTopicItem
import com.example.modapjt.data.dto.response.KeywordSearchResponse
import com.example.modapjt.data.dto.response.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SearchRepository(private val api: SearchApiService) {

    suspend fun getAutoCompleteKeywords(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("API_REQUEST", "서버에 요청 중: query=$query") // ✅ API 요청 로그 추가

                val response = api.getAutoCompleteKeywords(query)

                Log.d("API_SUCCESS", "서버 응답 성공: $response") // ✅ API 응답 성공 로그 추가

                response
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_ERROR", "API 요청 실패: ${e.message}") // ✅ API 오류 로그 추가
                emptyList()
            }
        }
    }


    suspend fun getInterestKeywords(keyword: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                api.getInterestKeywords(keyword) // ✅ 중복 제거
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    /**
     * 메인 페이지 데이터를 캐시 우선으로 가져옵니다.
     * 1. 캐시가 유효하면 → 서버 version 확인 → 동일하면 캐시 반환
     * 2. 캐시 없거나 version 불일치 → 서버에서 새로 조회 → 캐시 저장
     */
    suspend fun getSearchData(context: Context): SearchResponse? {
        return withContext(Dispatchers.IO) {
            try {
                // 캐시가 유효한 경우 version 비교
                if (MainPageCache.isValid(context)) {
                    val cachedVersion = MainPageCache.getVersion(context)
                    val serverVersion = try {
                        api.getMainPageVersion()["version"]
                    } catch (e: Exception) {
                        null
                    }
                    if (cachedVersion != null && cachedVersion == serverVersion) {
                        Log.d("SearchRepository", "캐시 히트 (version=$cachedVersion)")
                        return@withContext MainPageCache.load(context)
                    }
                }

                // 캐시 미스 또는 version 불일치 → 서버 조회
                val response = api.searchMain()
                val version = try {
                    api.getMainPageVersion()["version"] ?: ""
                } catch (e: Exception) {
                    ""
                }
                MainPageCache.save(context, response, version)
                Log.d("SearchRepository", "서버에서 새로 로드 (version=$version)")
                response
            } catch (e: Exception) {
                e.printStackTrace()
                // 네트워크 실패 시 캐시 반환 (오프라인 지원)
                MainPageCache.load(context)
            }
        }
    }

    /**
     * 기존 호환용 (context 없이 호출 시 캐시 없이 직접 API 호출)
     */
    suspend fun getSearchData(): SearchResponse? {
        return try {
            api.searchMain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getHomeKeyword(): HomeKeywordResponse {
        return try {
            api.homeKeyword() // 전체 HomeKeywordResponse 반환
        } catch (e: Exception) {
            Log.e("HomeKeywordRepository", "Error fetching home keyword", e)
            HomeKeywordResponse(emptyList(), "", emptyMap()) // 에러 발생 시 빈 데이터 반환
        }
    }

    suspend fun getSearchDataByKeyword(keyword: String): List<KeywordSearchResponse>? {
        return try {
            api.getSearchDataByKeyword(keyword) // ✅ API 응답을 리스트로 받음
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getHotTopics(limit: Int): List<HotTopicItem> {
        return try {
            api.getHotTopics(limit)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getKeywordUrl(keyword: String) {
        Log.d("SearchRepository", "Clicked keyword: $keyword")
    }



}

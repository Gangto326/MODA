//package com.example.modapjt.domain.viewmodel
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import com.example.modapjt.domain.model.Board
//import com.example.modapjt.domain.model.Card
//import java.time.LocalDateTime
//
//class BoardViewModel : ViewModel() {
//    // 상태 관리 변수 (기본 값은 빈 리스트)
//    var boards by mutableStateOf<List<Board>>(emptyList())
//        private set
//
//    // 데이터 가져오는 함수 (현재는 API 호출을 제외하고 하드코딩된 데이터만 사용)
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun fetchBoards() {
//        val fetchedBoards = getBoardDataFromAPI()  // 더미 데이터 반환
//        boards = fetchedBoards
//    }
//
//    // 더미 데이터를 반환하는 함수
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getBoardDataFromAPI(): List<Board> {
//        return listOf(
//            Board(
//                boardId = "1",
//                userId = "user1",
//                title = "게임 보드",
//                isPublic = true,
//                position = 1,
//                createdAt = LocalDateTime.now(),
//                isView = true,
//                cards = listOf(
//                    Card(
//                        cardId = "1",
//                        boardId = "1",
//                        typeId = 1,
//                        urlHash = null,
//                        title = "T1 : 프로의 세계는 냉정합니다",
//                        content = "티원 경기를 분석해보았습니다",
//                        embedding = null,
//                        createdAt = LocalDateTime.now(),
//                        updatedAt = null,
//                        deletedAt = null,
//                        isView = true,
//                    ),
//                    Card(
//                        cardId = "2",
//                        boardId = "1",
//                        typeId = 1,
//                        urlHash = null,
//                        title = "T1 vs KT | 매치 16 하이라이트",
//                        content = "LIVE & LIVE VOD",
//                        embedding = null,
//                        createdAt = LocalDateTime.of(2025, 1, 25, 0, 0),
//                        updatedAt = null,
//                        deletedAt = null,
//                        isView = false,
//                    ),
//                )
//            ),
//            Board(
//                boardId = "2",
//                userId = "user2",
//                title = "요리 보드",
//                isPublic = false,
//                position = 2,
//                createdAt = LocalDateTime.now(),
//                isView = false,
//                cards = listOf()
//            ),
//            Board(
//                boardId = "3",
//                userId = "user3",
//                title = "침착 보드",
//                isPublic = true,
//                position = 3,
//                createdAt = LocalDateTime.now(),
//                isView = false,
//                cards = listOf()
//            ),
//            Board(
//                boardId = "4",
//                userId = "user4",
//                title = "침착맨 보드",
//                isPublic = false,
//                position = 4,
//                createdAt = LocalDateTime.now(),
//                isView = false,
//                cards = listOf()
//            ),
//            Board(
//                boardId = "5",
//                userId = "user5",
//                title = "임시 보드",
//                isPublic = true,
//                position = 5,
//                createdAt = LocalDateTime.now(),
//                isView = false,
//                cards = listOf()
//            )
//        )
//    }
//
//    // 보드 데이터를 가져오는 함수
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getBoardData(boardId: String): Board {
//        return boards.firstOrNull { it.boardId == boardId }
//            ?: Board(boardId = "0", userId = "", title = "없음", isPublic = false, position = 0, createdAt = LocalDateTime.now(), isView = false, cards = emptyList())
//    }
//}
//


// BoardViewModel.kt
package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.BoardRepository
import com.example.modapjt.domain.model.Board
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BoardViewModel : ViewModel() {
    private val repository = BoardRepository()

    private val _boards = MutableStateFlow<List<Board>>(emptyList()) // 보드 리스트
    val boards = _boards.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // 로딩 상태
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null) // 에러 메시지
    val error = _error.asStateFlow()

    // API에서 보드 리스트 가져오기
    fun loadBoardList(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getBoardList(userId)
            result.onSuccess {
                _boards.value = it // UI에 반영
            }.onFailure {
                _error.value = it.message
                Log.e("BoardViewModel", "Error loading board list: ${it.message}")
            }

            _isLoading.value = false
        }
    }
}

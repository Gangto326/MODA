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

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.ModaRepository
import com.example.modapjt.domain.model.Board
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class BoardViewModel : ViewModel() {
    private val repository = ModaRepository()

    private val _boards = MutableStateFlow<List<Board>>(emptyList())
    val boards = _boards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _userId = MutableStateFlow("user1") // 현재 로그인된 유저 ID (임시)
    val userId = _userId.asStateFlow()

    init {
        loadMyBoards() // 초기값은 "내 보드"
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadBoards() {
//        // 현재는 더미 데이터 사용
//        _boards.value = repository.getDummyBoardList()
//
//        // 실제 API 연동시 사용할 코드 (주석처리)
//        /*
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                repository.getBoardList(getToken(), getNickName())
//                    .onSuccess { _boards.value = it }
//                    .onFailure { _error.value = it.message }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//        */
//    }

    fun loadBoardDetail(boardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 실제 API 연동시
                /*
                repository.getBoardDetail(boardId)
                    .onSuccess { board ->
                        _boards.value = listOf(board)
                    }
                    .onFailure { error ->
                        _error.value = error.message
                    }
                */

                // 현재는 더미 데이터 사용
                _boards.value = listOf(
                    repository.getDummyBoardList()
                        .find { it.boardId == boardId }
                        ?: throw Exception("보드를 찾을 수 없습니다.")
                )
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


//    // 내 보드만 보여주기 : 로그인된 유저 아이디 == 보드의 유저 아이디 동일한지 체크
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadMyBoards() {
//        // 현재는 더미 데이터를 사용하여 "내 보드" 필터링
//        val allBoards = repository.getDummyBoardList()
//        _boards.value = allBoards.filter { it.userId == _userId.value }
//
//        // 실제 API 연동시 사용할 코드
//        /*
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                repository.getBoardList(getToken(), getNickName())
//                    .onSuccess { boards ->
//                        _boards.value = boards.filter { it.userId == _userId.value }
//                    }
//                    .onFailure { _error.value = it.message }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//        */
//    }

    // 즐겨찾기한 보드
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadMyBookMarkBoards() {
        // 현재는 더미 데이터를 사용하여 "내 보드" 필터링
        val allBoards = repository.getDummyBoardList()
        _boards.value = allBoards.filter { it.userId != _userId.value }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun loadMyBoards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allBoards = repository.getDummyBoardList()
                _boards.value = allBoards.filter { it.userId == _userId.value }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadMyBookMarkBoards() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val allBoards = repository.getDummyBoardList()
//                _boards.value = allBoards.filter { it.isView } // 예제: 즐겨찾기한 보드 필터링
//            } catch (e: Exception) {
//                _error.value = e.message
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

}
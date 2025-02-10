package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = SearchRepository(RetrofitInstance.apiService)

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> get() = _searchResults

    fun fetchAutoCompleteKeywords(query: String) {
        viewModelScope.launch {
            _searchResults.value = repository.getAutoCompleteKeywords(query)
            Log.d("API_CALL", "API 호출: query=$query")
        }
    }

}

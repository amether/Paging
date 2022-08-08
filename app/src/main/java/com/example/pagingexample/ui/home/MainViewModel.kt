package com.example.pagingexample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pagingexample.data.model.Article
import com.example.pagingexample.data.network.EverythingNewsPagingSource
import com.example.pagingexample.data.network.NewsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val newsService: NewsService,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _news = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val news: StateFlow<PagingData<Article>> = _news.asStateFlow()

    init {
        viewModelScope.launch {
            _query.collect(::fetchPagingData)
        }
    }

    private suspend fun fetchPagingData(query: String) {
        val data = Pager(PagingConfig(5, enablePlaceholders = false)) {
            EverythingNewsPagingSource(newsService, query)
        }.flow.first()
        _news.value = data
    }

    fun setQuery(query: String) {
        _query.tryEmit(query)
    }
}

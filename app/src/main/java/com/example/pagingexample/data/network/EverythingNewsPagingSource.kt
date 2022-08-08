package com.example.pagingexample.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingexample.data.model.Article
import com.example.pagingexample.data.toArticle
import retrofit2.HttpException

class EverythingNewsPagingSource(
    private val newsService: NewsService,
    private val query: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }

        try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(NewsService.MAX_PAGE_SIZE)
            val response = newsService.getNews(query, pageNumber, pageSize)

            if (response.isSuccessful) {
                val articles = response.body()!!.articles.map { it.toArticle() }
                val nextPageNumber = if (articles.isEmpty()) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                return LoadResult.Page(articles, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
package com.example.pagingexample.data.network.model

import androidx.annotation.IntRange
import com.example.pagingexample.data.network.model.ArticleDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticlesResponseDto(
    @SerialName("status") val status: String,
    @SerialName("totalResults") @IntRange(from = 1) val totalResults: Int,
    @SerialName("message") val message: String? = null,
    @SerialName("articles") val articles: List<ArticleDto>,
)
package com.example.pagingexample.data

import com.example.pagingexample.data.model.Article
import com.example.pagingexample.data.model.Source
import com.example.pagingexample.data.network.model.ArticleDto
import com.example.pagingexample.data.network.model.SourceDto

internal fun ArticleDto.toArticle(): Article {
    return Article(
        source = this.source?.toSource(),
        title = title,
        url = url,
        description = description,
        author = author,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

private fun SourceDto.toSource(): Source {
    return Source(id = id, name = name)
}

package com.example.pagingexample.di

import com.example.pagingexample.data.network.AuthInterceptor
import com.example.pagingexample.data.network.EverythingNewsPagingSource
import com.example.pagingexample.data.network.NewsService
import com.example.pagingexample.ui.home.MainViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val koinModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor("a3aebd96c7c94de09b552a2aee1675d4"))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .client(get())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<NewsService> { get<Retrofit>().create(NewsService::class.java)}
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

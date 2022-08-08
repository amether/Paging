package com.example.pagingexample

import android.app.Application
import com.example.pagingexample.di.koinModule
import com.example.pagingexample.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.dsl.module


class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(
                koinModule,
                viewModelModule
            ))
        }
    }
}
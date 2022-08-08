package com.example.pagingexample.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pagingexample.R
import com.example.pagingexample.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity(R.layout.activity_home) {

    private val viewBinding by viewBinding(ActivityHomeBinding::bind)
    private val viewModel: MainViewModel by viewModel()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeNewsAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewBinding) {
            news.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsLoaderStateAdapter(),
                footer = NewsLoaderStateAdapter()
            )
            query.doAfterTextChanged { text ->
                viewModel.setQuery(text?.toString() ?: "")
            }
        }

        adapter.addLoadStateListener { state ->
            with(viewBinding) {
                news.isVisible = state.refresh != LoadState.Loading
                progress.isVisible = state.refresh == LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.news
                .collectLatest(adapter::submitData)
        }

        viewModel.query
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach(::updateSearchQuery)
            .launchIn(lifecycleScope)
    }

    private fun updateSearchQuery(searchQuery: String) {
        with(viewBinding.query) {
            if ((text?.toString() ?: "") != searchQuery) {
                setText(searchQuery)
            }
        }
    }
}
package com.denbond7.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.denbond7.myapplication.databinding.FragmentMainBinding
import com.denbond7.myapplication.ui.adapters.ProgressLoadStateAdapter
import com.denbond7.myapplication.ui.adapters.UserAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val pagingAdapter = UserAdapter()
    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.layoutManager = layoutManager
        binding?.recyclerView?.setHasFixedSize(true)
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation)
        )
        binding?.recyclerView?.adapter =
            pagingAdapter.withLoadStateFooter(ProgressLoadStateAdapter())
        setupMainViewModel()
    }

    private fun setupMainViewModel() {
        @OptIn(ExperimentalCoroutinesApi::class)
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.pagerFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadState ->
                Log.d("DDDD", "$loadState")
                val isDataLoadingFromAnyResourcesAtStart =
                    loadState.source.refresh is LoadState.Loading || loadState.mediator?.refresh is LoadState.Loading

                val showEmptyView =
                    loadState.source.refresh is LoadState.NotLoading
                            && loadState.source.prepend is LoadState.NotLoading
                            && loadState.source.append is LoadState.NotLoading && loadState.source.append.endOfPaginationReached
                            && loadState.mediator?.refresh is LoadState.NotLoading
                            && loadState.mediator?.prepend is LoadState.NotLoading
                            && loadState.mediator?.append is LoadState.NotLoading && loadState.mediator?.append?.endOfPaginationReached == true

                when {
                    isDataLoadingFromAnyResourcesAtStart && pagingAdapter.itemCount == 0 -> {
                        //showProgress()
                        binding?.progressBar?.visibility = View.VISIBLE
                        binding?.recyclerView?.visibility = View.GONE
                        binding?.textViewNoResults?.visibility = View.GONE
                    }

                    showEmptyView && pagingAdapter.itemCount == 0 -> {
                        //showEmptyView()
                        binding?.progressBar?.visibility = View.GONE
                        binding?.recyclerView?.visibility = View.GONE
                        binding?.textViewNoResults?.visibility = View.VISIBLE
                    }

                    pagingAdapter.itemCount > 0 -> {
                        //showContent()
                        binding?.progressBar?.visibility = View.GONE
                        binding?.recyclerView?.visibility = View.VISIBLE
                        binding?.textViewNoResults?.visibility = View.GONE
                    }
                }
            }
        }
    }
}

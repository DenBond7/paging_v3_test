package com.denbond7.myapplication.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.denbond7.myapplication.R
import com.denbond7.myapplication.database.AppDatabase
import com.denbond7.myapplication.databinding.FragmentMainBinding
import com.denbond7.myapplication.ui.adapters.ProgressLoadStateAdapter
import com.denbond7.myapplication.ui.adapters.UserAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private val pagingAdapter = UserAdapter() {
        lifecycleScope.launch {
            val roomDatabase = AppDatabase.getDatabase(requireContext())
            roomDatabase.userDao().deleteByUid(it)
        }
    }
    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupMainViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemReset -> {
                lifecycleScope.launch {
                    val roomDatabase = AppDatabase.getDatabase(requireContext())
                    roomDatabase.userDao().deleteAll()
                    Toast.makeText(requireContext(), R.string.reload, Toast.LENGTH_SHORT).show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        binding?.recyclerView?.layoutManager = layoutManager
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(context, layoutManager.orientation)
        )
        binding?.recyclerView?.adapter = pagingAdapter.withLoadStateFooter(
            ProgressLoadStateAdapter()
        )
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
                val isDataLoadingFromAnyResourcesAtStart =
                    loadState.source.refresh is LoadState.Loading
                            || loadState.source.prepend is LoadState.Loading
                            || loadState.source.append is LoadState.Loading
                            || loadState.mediator?.refresh is LoadState.Loading
                            || loadState.mediator?.prepend is LoadState.Loading
                            || loadState.mediator?.append is LoadState.Loading

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

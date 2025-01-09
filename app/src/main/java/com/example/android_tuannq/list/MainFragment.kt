package com.example.android_tuannq.list

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.viewbinding.ViewBinding
import com.example.android_tuannq.R
import com.example.android_tuannq.databinding.LayoutMainBinding
import com.karleinstein.basemvvm.base.BaseFragment
import com.karleinstein.basemvvm.extension.showToast
import com.karleinstein.basemvvm.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.layout_main) {
    override val viewBinding: LayoutMainBinding? by viewBinding(LayoutMainBinding::bind)

    override val viewModel: MainViewModel by viewModels()
    private val adapter: GithubUserAdapter by lazy {
        GithubUserAdapter {
            val action = MainFragmentDirections.actionToDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun bindView() {
        viewModel.getUsers()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorResponse.collect {
                        if (!it?.message.isNullOrEmpty() && isAdded) context?.showToast(
                            it?.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                launch {
                    viewModel.listUser.collect {
                        if (it == null) return@collect
                        adapter.submitData(it)
                    }
                }
            }
        }
        adapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Error -> showError(loadState.refresh as? LoadState.Error)

                loadState.append is LoadState.Error -> showError(loadState.append as? LoadState.Error)

                loadState.prepend is LoadState.Error -> showError(loadState.prepend as? LoadState.Error)
            }
        }
    }

    private fun showError(error: LoadState.Error?) {
        if (!error?.error?.message.isNullOrEmpty() && isAdded) context?.showToast(
            error?.error?.message.orEmpty(),
            Toast.LENGTH_SHORT
        )
    }

    override fun setUpView() {
        viewBinding?.toolbar?.setNavigationIcon(R.drawable.ic_back)
        viewBinding?.recycler?.adapter = adapter.withLoadStateFooter(LoadProgressStateAdapter())
    }
}
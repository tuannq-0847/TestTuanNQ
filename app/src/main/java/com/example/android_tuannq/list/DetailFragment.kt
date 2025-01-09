package com.example.android_tuannq.list

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.android_tuannq.R
import com.example.android_tuannq.databinding.LayoutDetailUserFragmentBinding
import com.karleinstein.basemvvm.base.BaseFragment
import com.karleinstein.basemvvm.extension.showToast
import com.karleinstein.basemvvm.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment(R.layout.layout_detail_user_fragment) {
    override val viewBinding: LayoutDetailUserFragmentBinding? by viewBinding(
        LayoutDetailUserFragmentBinding::bind
    )

    private val args: DetailFragmentArgs by navArgs()

    override val viewModel: MainViewModel by viewModels()

    override fun bindView() {
        viewModel.getDetail(args.userName)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorResponse.collect {
                        if (!it?.message.isNullOrEmpty() && isAdded) context?.showToast(
                            it?.message.orEmpty(), Toast.LENGTH_SHORT
                        )
                    }
                }
                launch {
                    viewModel.detailUser.collect { detail ->
                        viewBinding?.textName?.text = detail?.name
                        viewBinding?.textLocation?.text = detail?.location
                        viewBinding?.textHtmlUrl?.text = detail?.htmlUrl
                        context?.let {
                            Glide.with(it).load(detail?.url)
                                .into(viewBinding?.imageAvt ?: return@collect)
                        }
                    }
                }
            }
        }
    }

    override fun setUpView() {
        viewBinding?.toolbar?.setNavigationIcon(R.drawable.ic_back)
        viewBinding?.toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
    }
}
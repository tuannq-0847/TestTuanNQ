package com.example.android_tuannq

import com.example.android_tuannq.databinding.LayoutMainActivityBinding
import com.karleinstein.basemvvm.base.BaseActivity
import com.karleinstein.basemvvm.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override val viewBinding: LayoutMainActivityBinding? by viewBinding(LayoutMainActivityBinding::inflate)

    override fun bindView() {
    }
    override fun setUpView() {

    }
}

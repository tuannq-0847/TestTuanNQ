package com.example.android_tuannq.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_tuannq.R
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.databinding.LayoutDetailUserBinding
import com.karleinstein.basemvvm.base.BaseRecyclerAdapter
import com.karleinstein.basemvvm.base.BaseViewHolder
import com.karleinstein.basemvvm.extension.onThrottledClick

class GithubUserAdapter(private val click: (userName: String) -> Unit) :
    PagingDataAdapter<User, BaseViewHolder>(object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }
    }) {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.binding<LayoutDetailUserBinding>()?.apply {
            textName.text = getItem(position)?.name
            textUrl.text = getItem(position)?.htmlUrl
            Glide.with(imageAvt.context).load(getItem(position)?.url).into(imageAvt)
            root.onThrottledClick {
                click.invoke(getItem(position)?.name.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.layout_detail_user, parent, false
            )
        )
    }
}

class FooterVH(view: View) : RecyclerView.ViewHolder(view)

// Adapter that displays a loading spinner when
// state is LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class LoadProgressStateAdapter() : LoadStateAdapter<FooterVH>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): FooterVH {
        return FooterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loadmore, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: FooterVH, loadState: LoadState
    ) = Unit
}

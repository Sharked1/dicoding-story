package com.dicoding.mystoryapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.databinding.ListStoryBinding
import com.dicoding.mystoryapp.view.detail.DetailActivity

class StoryListPagingAdapter: PagingDataAdapter<ListStoryItem, StoryListPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ListStoryItem) {
            binding.tvName.text = listStory.name
            binding.tvDescription.text = listStory.description
            Glide.with(itemView.context)
                .load(listStory.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(binding.ivStory)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java)
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(binding.ivStory, "image"),
                    Pair(binding.tvName, "name"),
                    Pair(binding.tvDescription, "description")
                )
                intent.putExtra(DetailActivity.EXTRA_ID, listStory.id)
                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
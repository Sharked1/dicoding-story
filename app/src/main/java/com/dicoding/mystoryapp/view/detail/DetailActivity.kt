package com.dicoding.mystoryapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.databinding.ActivityDetailBinding
import com.dicoding.mystoryapp.formatDateTime
import com.dicoding.mystoryapp.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

    }

    private fun setupView(){
        val id = intent.getStringExtra(EXTRA_ID)
        viewModel.getDetail(id!!)

        viewModel.imgUrl.observe(this) {
            Glide.with(this)
                .load(it)
                .into(binding.ivStory)
        }

        viewModel.name.observe(this) {
            binding.tvName.text = it
        }

        viewModel.description.observe(this) {
            binding.tvDescription.text = it
        }

        viewModel.createdAt.observe(this) {
            val (date, time) = formatDateTime(it)
            binding.tvTime.text = time
            binding.tvDate.text = date
        }

    }

    companion object {
        const val EXTRA_ID = "id"
        const val EXTRA_IMG_URL = "url"
    }
}
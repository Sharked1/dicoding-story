package com.dicoding.mystoryapp.view.upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityUploadStoryBinding

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
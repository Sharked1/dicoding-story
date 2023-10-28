package com.dicoding.mystoryapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mycamerastarter.data.api.ApiConfig
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else {
                ApiConfig.setToken(user.token!!)
                setupAppBar()
                setupListStory()
            }
        }


    }

    private fun setupAppBar(){
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_logout ->{
                    AlertDialog.Builder(this).apply {
                        setTitle("Logout")
                        setMessage(ContextCompat.getString(context, R.string.logout_description))
                        setPositiveButton(ContextCompat.getString(context, R.string.yes)) { _, _ ->
                            viewModel.logout()
                        }
                        setNegativeButton(ContextCompat.getString(context, R.string.no)) { _, _ ->

                        }
                        create()
                        show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupListStory(){
        viewModel.getStories(1)
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        val adapter = StoryListAdapter()
        viewModel.getStories(1).observe(this@MainActivity) {
            if (!it.error){
                val storyList = it.listStory.map {story ->
                    ListStoryItem(
                        story.photoUrl,
                        story.createdAt,
                        story.name,
                        story.description,
                        story.lon,
                        story.id,
                        story.lat
                    )
                }
               adapter.submitList(storyList)
            }
            else {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.addItemDecoration(itemDecoration)
        binding.rvStory.adapter = adapter
    }
}
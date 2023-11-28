package com.dicoding.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mycamerastarter.data.api.ApiConfig
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.dicoding.mystoryapp.view.maps.MapsActivity
import com.dicoding.mystoryapp.view.upload.UploadStoryActivity
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity

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
                setupAppBar(user.email)
                setupListStory()
                setupAction()
            }
        }
    }

    private fun setupAppBar(email: String){
        val message = getString(R.string.logout_description, email)
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_logout ->{
                    AlertDialog.Builder(this).apply {
                        setTitle("Logout")
                        setMessage(message)
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

                R.id.language_setting -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.map_activity -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupListStory(){
        val layoutManager = LinearLayoutManager(this)
        val adapter = StoryListPagingAdapter()
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.dvider_line)!!)
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.addItemDecoration(itemDecoration)
        binding.rvStory.adapter = adapter.withLoadStateFooter(LoadingAdapter { adapter.retry() })

        viewModel.getStories().observe(this@MainActivity) {
            adapter.submitData(lifecycle, it)
        }

    }

    private fun setupAction(){
        binding.fabUploadStory.setOnClickListener{
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.dicoding.mystoryapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.view.ViewModelFactory
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
        }

        setupAppBar()

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
                            finish()
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
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.addItemDecoration(itemDecoration)
    }
}
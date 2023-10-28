package com.dicoding.mystoryapp.view.register

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel

class RegisterViewModel(private val repository: UserRepository): ViewModel() {
    fun registerUser(user: UserModel) = repository.registerUser(user)
}
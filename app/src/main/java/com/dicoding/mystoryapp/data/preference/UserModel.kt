package com.dicoding.picodiploma.loginwithanimation.data.pref

data class UserModel(
    val email: String,
    val name: String,
    val password: String,
    val token: String? = null,
    val isLogin: Boolean = false
)
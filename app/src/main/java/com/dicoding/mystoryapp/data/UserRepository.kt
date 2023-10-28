package com.dicoding.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.mycamerastarter.data.api.ApiService
import com.dicoding.mystoryapp.data.api.LoginResponse
import com.dicoding.mystoryapp.data.api.LoginResult
import com.dicoding.mystoryapp.data.api.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(user: UserModel): LiveData<RegisterResponse> = liveData {
        try {
            val response = apiService.registerUser(user.name, user.email, user.password)
            emit(response)
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("Register process", "Error: $errorMessage")
            emit(RegisterResponse(true, errorMessage))
        }
    }

    fun loginUser(user: UserModel): LiveData<LoginResponse> = liveData {
        try {
            val response = apiService.loginUser(user.email, user.password)
            emit(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("Login process", "Error: ${errorMessage}")
            emit(LoginResponse(LoginResult("", "", ""), true, errorMessage))
        }
    }

    suspend fun getStories(page: Int){

    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
package com.dicoding.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mycamerastarter.data.api.ApiConfig
import com.dicoding.mycamerastarter.data.api.ApiService
import com.dicoding.mystoryapp.data.api.AddNewStoryResponse
import com.dicoding.mystoryapp.data.api.DetailStoryResponse
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.data.api.LoginResponse
import com.dicoding.mystoryapp.data.api.LoginResult
import com.dicoding.mystoryapp.data.api.RegisterResponse
import com.dicoding.mystoryapp.data.api.Story
import com.dicoding.mystoryapp.data.api.StoryListResponse
import com.dicoding.mystoryapp.data.api.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        ApiConfig.setToken(user.token!!)
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        val user = userPreference.getSession()
        return user
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

    fun getStories() : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { StoryPagingSource(apiService) }
        ).liveData
    }

    suspend fun getDetail(id: String): DetailStoryResponse {
        try {
            val response = apiService.getDetail(id)
            return response
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryListResponse::class.java)
            Log.d("getDetail", "Error: ${errorBody.message}")
            return DetailStoryResponse(true, errorBody.message, Story("", "", "", "", "", "", ""))
        }
    }

    fun uploadImage(multipartBody: MultipartBody.Part, desc: RequestBody, lat: Double?, lon: Double?) = liveData<AddNewStoryResponse>{
        try {
            val response = apiService.uploadImage(multipartBody, desc, lat, lon)
            emit(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, AddNewStoryResponse::class.java)
            Log.d("uploadImage", "Error ${errorBody.message}")
            emit(AddNewStoryResponse(true, errorBody.message))
        }
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
    fun getStoriesWithLocation() = liveData<StoryListResponse>{
        try {
            val response = apiService.getStoriesWithLocation()
            emit(response)
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryListResponse::class.java)
            Log.d("getStories", "Error: ${errorBody.message}")
            emit(StoryListResponse(listOf(ListStoryItem("", "", "", "", "", "", "")), true, errorBody.message))
        }
    }

}
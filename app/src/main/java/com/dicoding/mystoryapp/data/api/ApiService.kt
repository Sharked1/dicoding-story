package com.dicoding.mycamerastarter.data.api

import com.dicoding.mystoryapp.data.api.DetailStoryResponse
import com.dicoding.mystoryapp.data.api.LoginResponse
import com.dicoding.mystoryapp.data.api.RegisterResponse
import com.dicoding.mystoryapp.data.api.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part ("description") description: RequestBody
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field ("name") name: String,
        @Field ("email") email: String,
        @Field ("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field ("email") email: String,
        @Field ("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query ("page") page: Int
    ): StoryListResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Path ("id") id: String
    ): DetailStoryResponse

}
package com.dicoding.mystoryapp.view.upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.data.api.AddNewStoryResponse
import com.dicoding.mystoryapp.reduceFileImage
import com.dicoding.mystoryapp.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadStoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun uploadImage(multipartBody: MultipartBody.Part, desc: RequestBody, lat: Double?, lon: Double?): LiveData<AddNewStoryResponse> {
        return userRepository.uploadImage(multipartBody, desc, lat, lon)
    }
}
package com.dicoding.mystoryapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.data.api.StoryListResponse

class MapsViewModel(private val repository: UserRepository) : ViewModel()  {

    fun getStoriesWithLocation(): LiveData<StoryListResponse> {
        return repository.getStoriesWithLocation()
    }

}
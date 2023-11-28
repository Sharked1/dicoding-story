package com.dicoding.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.data.api.StoryListResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {

    private val _mStoryList = MutableLiveData<List<StoryListResponse>>()
    val storyList: LiveData<List<StoryListResponse>> = _mStoryList

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return repository.getStories().cachedIn(viewModelScope)
    }


}
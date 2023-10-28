package com.dicoding.mystoryapp.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.data.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository): ViewModel() {
    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description : LiveData<String> = _description

    private val _createdAt = MutableLiveData<String>()
    val createdAt : LiveData<String> = _createdAt

    private val _imgUrl = MutableLiveData<String>()
    val imgUrl : LiveData<String> = _imgUrl

    fun getDetail(id: String){
        viewModelScope.launch {
            val response = repository.getDetail(id)
            _name.value = response.story?.name
            _description.value = response.story?.description
            _createdAt.value = response.story?.createdAt
            _imgUrl.value = response.story?.photoUrl
        }
    }
}
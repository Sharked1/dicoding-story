package com.dicoding.mystoryapp.util

import com.dicoding.mystoryapp.data.api.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val stories : MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            stories.add(ListStoryItem("photoUrl$1", "$i", "my name is $i", "description for $i story", "$i", "$i", "$i")
            )
        }
        return stories
    }
}
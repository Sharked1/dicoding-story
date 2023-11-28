package com.dicoding.mystoryapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.data.api.ListStoryItem
import com.dicoding.mystoryapp.util.DataDummy
import com.dicoding.mystoryapp.util.MainDispatcherRule
import com.dicoding.mystoryapp.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `when getStories Should Not null And return Data`() = runTest {
        val dummyData = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> =StoryPagingSource.snapshot(dummyData)
        val expectedData =MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value =data
        `when`(userRepository.getStories()).thenReturn(expectedData)
        val viewModel = MainViewModel(userRepository)
        val actualData = viewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyData.size, differ.snapshot().size)
        Assert.assertEquals(dummyData[0], differ.snapshot()[0])

    }

    @Test
    fun `when getStories empty should return no data`() = runTest {
        val data : PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data
        `when`(userRepository.getStories()).thenReturn(expectedData)

        val viewModel = MainViewModel(userRepository)
        val actualData = viewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualData)

        Assert.assertEquals(0, differ.snapshot().size)

    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}
class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
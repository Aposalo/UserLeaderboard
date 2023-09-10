package android.example.com.userleaderboard.model

import android.example.com.userleaderboard.api.dataclass.Page
import android.example.com.userleaderboard.repository.UserLeaderboardRepository
import android.example.com.userleaderboard.util.Constants
import android.example.com.userleaderboard.util.Resource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class UserLeaderboardModel(
    private val repository: UserLeaderboardRepository
) : ViewModel() {

    val pageData: MutableLiveData<Resource<Page>> = MutableLiveData()
    var currentPageNumber = 0
    private lateinit var pageResponse: Page
    init {
        getUserPage()
    }

    fun getItemModels() : MutableList<ItemModel> {
        val itemsModel = ArrayList<ItemModel>(Constants.QUERY_PAGE_SIZE)
        val totalCount = Constants.QUERY_PAGE_SIZE - 1
        for (i in 0..totalCount)
        {
            pageResponse.apply {
                items[i].apply {
                    val itemModel = ItemModel (
                        i + 1,
                        user.avatar,
                        user.name,
                        score
                    )
                    itemsModel.add(itemModel)
                }
            }
        }
        return itemsModel
    }

    fun getUserPage() =  viewModelScope.launch {
        pageData.postValue(Resource.Loading())
        val response = repository.getUserPage(currentPageNumber)
        pageData.postValue(handlePageResponse(response))
    }

    private fun handlePageResponse(response: Response<Page>) : Resource<Page> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                currentPageNumber++
                pageResponse = resultResponse
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
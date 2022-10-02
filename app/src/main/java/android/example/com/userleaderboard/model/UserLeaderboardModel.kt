package android.example.com.userleaderboard.model

import android.example.com.userleaderboard.util.Resource
import android.example.com.userleaderboard.api.dataclass.Page
import android.example.com.userleaderboard.repository.UserLeaderboardRepository
import android.example.com.userleaderboard.util.Constants
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
    var pageResponse: Page? = null

    init {
        getUserPage()
    }

    fun getItemModels() : MutableList<ItemModel> {
        val itemsModel = ArrayList<ItemModel>(Constants.QUERY_PAGE_SIZE)
        var i = 0
        val totalCount = Constants.QUERY_PAGE_SIZE - 1
        for (i in (0..totalCount))
        {
            val itemModel = ItemModel (
                i + 1,
                pageResponse?.items!![i].user.avatar,
                pageResponse?.items!![i].user.name,
                pageResponse?.items!![i].score
            )

            itemsModel.add(itemModel)
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
//                if(pageResponse == null) {
//                    pageResponse = resultResponse
//                } else {
//                    val oldItems = pageResponse?.items
//                    val newItems = resultResponse.items
//                    oldItems?.addAll(newItems)
//                }
//                return Resource.Success(pageResponse ?: resultResponse)
                pageResponse = resultResponse
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
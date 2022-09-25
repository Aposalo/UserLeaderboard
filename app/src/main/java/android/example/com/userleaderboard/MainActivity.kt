package android.example.com.userleaderboard

import android.content.res.Configuration
import android.example.com.userleaderboard.model.UserLeaderboardModel
import android.example.com.userleaderboard.repository.UserLeaderboardRepository
import android.example.com.userleaderboard.util.Constants.Companion.QUERY_PAGE_SIZE
import android.example.com.userleaderboard.util.Resource
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_list.*


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: UserLeaderboardModel

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                   isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getUserPage()
                isScrolling = false
            } else {
                rvUsers.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private lateinit var userLeaderboardAdapter : UserLeaderboardAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = UserLeaderboardRepository()
        viewModel = UserLeaderboardModel(repository)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        viewModel.pageData.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        userLeaderboardAdapter.differ.submitList(newsResponse.items.toList())
                        val totalPages = newsResponse.totalItems / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.currentPageNumber == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun setupRecyclerView() {
        userLeaderboardAdapter = UserLeaderboardAdapter()
        rvUsers.apply{
            adapter = userLeaderboardAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }

}
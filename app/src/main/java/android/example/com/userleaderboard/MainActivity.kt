package android.example.com.userleaderboard

import android.example.com.userleaderboard.adapter.UserLeaderboardAdapter
import android.example.com.userleaderboard.model.UserLeaderboardModel
import android.example.com.userleaderboard.repository.UserLeaderboardRepository
import android.example.com.userleaderboard.util.Constants.Companion.QUERY_PAGE_SIZE
import android.example.com.userleaderboard.util.Resource
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserLeaderboardModel

    private lateinit var userLeaderboardAdapter : UserLeaderboardAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
            if (shouldPaginate) {
                viewModel.getUserPage()
                isScrolling = false
            }
            else {
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
        var mainToolbar = findViewById<Toolbar>(R.id.toolbar)
        var mainToolbarTextView = toolbar.findViewById<TextView>(R.id.toolbarText)
        toolbar.title = ""
        setSupportActionBar(mainToolbar)

        setupRecyclerView()

        viewModel.pageData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        userLeaderboardAdapter.differ.submitList(viewModel.getItemModels())
                        isLastPage = viewModel.currentPageNumber == newsResponse.totalPages
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
        }
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
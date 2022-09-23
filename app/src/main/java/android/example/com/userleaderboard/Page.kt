package android.example.com.userleaderboard

data class Page(
    val currentPage: Int,
    val items: List<Item>,
    val nextPage: Int,
    val previousPage: Any,
    val timeUntilReset: String,
    val totalItems: Int,
    val totalPages: Int
)
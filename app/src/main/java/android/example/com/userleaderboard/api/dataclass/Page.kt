package android.example.com.userleaderboard.api.dataclass

data class Page(
    val currentPage: Int,
    val items: MutableList<Item>,
    val nextPage: Int,
    val previousPage: Any,
    val timeUntilReset: String,
    val totalItems: Int,
    val totalPages: Int
)
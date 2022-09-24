package android.example.com.userleaderboard

import android.example.com.userleaderboard.api.dataclass.Item
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.example.com.userleaderboard.databinding.UserListBinding
import android.view.LayoutInflater
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class UserLeaderboardAdapter : RecyclerView.Adapter<UserLeaderboardAdapter.PageViewHolder>() {

    inner class PageViewHolder(val binding: UserListBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.user.name == newItem.user.name &&
                    oldItem.score == newItem.score
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, diffCallback)
    var items: List<Item>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(UserListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            val rank = position + 1
            tvRank.text = rank.toString()
            //tvAvatar.text = item.user.avatar
            tvName.text = item.user.name
            tvPoints.text = item.score }
    }


}
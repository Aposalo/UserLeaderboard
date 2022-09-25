package android.example.com.userleaderboard

import android.example.com.userleaderboard.api.dataclass.Item
import android.example.com.userleaderboard.util.BitmapExtension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import kotlinx.android.synthetic.main.user_list.view.*


class UserLeaderboardAdapter : RecyclerView.Adapter<UserLeaderboardAdapter.PageViewHolder>() {

    inner class PageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

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
        return PageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_list,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.itemView.apply {
            val item = items[position]
            val rank = position + 1
            tvRank.text = rank.toString()

            val avatar = GlideUrl (
                item.user.avatar, LazyHeaders.Builder()
                    .addHeader("User-Agent", "5")
                    .build()
            )

            Glide.with(this)
                .load(avatar)
                .circleCrop()
                .into(tvAvatar)

            tvName.text = item.user.name
            tvPoints.text = item.score
            tvStarIcon.setImageBitmap(BitmapExtension.getImageBitmap(
                R.drawable.star_icon_leaderboard,
                resources))
        }
    }
}
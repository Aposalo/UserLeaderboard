package android.example.com.userleaderboard.adapter

import android.example.com.userleaderboard.R
import android.example.com.userleaderboard.model.ItemModel
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

    private val diffCallback = object : DiffUtil.ItemCallback<ItemModel>() {

        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.score == newItem.score &&
                    oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    var itemModels: List<ItemModel>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return itemModels.size
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
            val itemModel = itemModels[position]
            tvRank.text = itemModel.id.toString()

            val avatar = GlideUrl (
                itemModel.avatar, LazyHeaders.Builder()
                    .addHeader("User-Agent", "5")
                    .build()
            )

            Glide.with(this)
                .load(avatar)
                .circleCrop()
                .into(tvAvatar)

            tvName.text = itemModel.name
            tvPoints.text = itemModel.score
            tvStarIcon.setImageBitmap(BitmapExtension.getImageBitmap(
                R.drawable.star_icon_leaderboard,
                resources))
        }
    }
}
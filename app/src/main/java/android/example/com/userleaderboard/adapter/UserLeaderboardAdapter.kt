package android.example.com.userleaderboard.adapter

import android.example.com.userleaderboard.R
import android.example.com.userleaderboard.databinding.UserListBinding
import android.example.com.userleaderboard.model.ItemModel
import android.example.com.userleaderboard.util.BitmapExtension
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

class UserLeaderboardAdapter : RecyclerView.Adapter<UserLeaderboardAdapter.PageViewHolder>() {

    inner class PageViewHolder(val binding: UserListBinding): RecyclerView.ViewHolder(binding.root)

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

    private var itemModels: List<ItemModel>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return itemModels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            binding = UserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        with(holder) {
            with(itemModels[position]) {
                binding.tvRank.text = this.id.toString()
                Glide.with(binding.root)
                    .load(getFormattedAvatarIcon())
                    .circleCrop()
                    .into(binding.tvAvatar)
                binding.tvName.text = this.name
                binding.tvPoints.text = this.score
                binding.tvStarIcon.setImageBitmap(
                    BitmapExtension.getImageBitmap(
                        R.drawable.star_icon_leaderboard,
                        itemView.resources
                    )
                )
            }
        }
    }

    private fun ItemModel.getFormattedAvatarIcon() =
        GlideUrl(
            this.avatar, LazyHeaders.Builder()
                .addHeader("User-Agent", "5")
                .build()
        )
}
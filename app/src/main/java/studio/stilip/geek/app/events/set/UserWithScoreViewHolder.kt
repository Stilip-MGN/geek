package studio.stilip.geek.app.events.set

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardMemberScoreVisitorBinding
import studio.stilip.geek.domain.entities.UserWithScore

class UserWithScoreViewHolder(
    private val binding: CardMemberScoreVisitorBinding,
    private val onItemClicked: (UserWithScore) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var userWithScore: UserWithScore

    init {
        itemView.setOnClickListener {
            onItemClicked(userWithScore)
        }
    }

    fun bind(item: UserWithScore) = with(binding) {
        userWithScore = item


        score.text = userWithScore.score.toString()
        Glide.with(avatar)
            .load(userWithScore.user.avatar)
            .centerCrop()
            .into(avatar)

        name.text = userWithScore.user.name
    }
}
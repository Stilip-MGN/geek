package studio.stilip.geek.app.events.event_visitor.round.score

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardMemberScoreVisitorBinding
import studio.stilip.geek.domain.entities.Score

class MemberScoreVisitorViewHolder(
    private val binding: CardMemberScoreVisitorBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var scoreItem: Score

    fun bind(item: Score) = with(binding) {
        scoreItem = item
        if (scoreItem.members.isNotEmpty() && scoreItem.memberId.isNotEmpty()) {
            val member = scoreItem.members.first { m -> m.id == scoreItem.memberId }

            Glide.with(avatar)
                .load(member.avatar)
                .centerCrop()
                .into(avatar)

            name.text = member.name
            score.text = scoreItem.score.toString()
        }
    }
}
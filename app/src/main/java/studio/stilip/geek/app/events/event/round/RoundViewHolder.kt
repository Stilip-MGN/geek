package studio.stilip.geek.app.events.event.round

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.app.events.event.round.score.MemberScoreAdapter
import studio.stilip.geek.databinding.CardRoundBinding
import studio.stilip.geek.domain.entities.Round
import studio.stilip.geek.domain.entities.Score

class RoundViewHolder(
    private val binding: CardRoundBinding,
    private val onMemberChanged: (Round, Score) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var round: Round

    fun bind(item: Round) = with(binding) {
        round = item

        title.text = round.title
        val adapter = MemberScoreAdapter { score ->
            onMemberChanged(round, score)
        }
        adapter.submitList(round.scores)
        with(binding) {
            recScore.adapter = adapter
        }

    }
}
package studio.stilip.geek.app.events.event_visitor.round

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.app.events.event_visitor.round.score.MemberScoreVisitorAdapter
import studio.stilip.geek.databinding.CardRoundVisitorBinding
import studio.stilip.geek.domain.entities.Round

class RoundVisitorViewHolder(
    private val binding: CardRoundVisitorBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var round: Round

    fun bind(item: Round) = with(binding) {
        round = item

        title.text = round.title
        val adapter = MemberScoreVisitorAdapter()

        adapter.submitList(round.scores)
        with(binding) {
            recScore.adapter = adapter
        }
    }
}
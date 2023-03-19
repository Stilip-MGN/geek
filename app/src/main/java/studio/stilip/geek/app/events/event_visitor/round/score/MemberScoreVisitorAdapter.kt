package studio.stilip.geek.app.events.event_visitor.round.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.events.event.round.score.ScoreDiffCallback
import studio.stilip.geek.databinding.CardMemberScoreVisitorBinding
import studio.stilip.geek.domain.entities.Score

class MemberScoreVisitorAdapter :
    ListAdapter<Score, MemberScoreVisitorViewHolder>(ScoreDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemberScoreVisitorViewHolder(
            CardMemberScoreVisitorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )

    override fun onBindViewHolder(holder: MemberScoreVisitorViewHolder, position: Int) =
        holder.bind(getItem(position))

}
package studio.stilip.geek.app.events.event.round.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardMemberScoreBinding
import studio.stilip.geek.domain.entities.Score

class MemberScoreAdapter(
    private val onMemberChanged: (Score) -> Unit,
) : ListAdapter<Score, MemberScoreViewHolder>(ScoreDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemberScoreViewHolder(
            parent.context,
            CardMemberScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onMemberChanged
        )

    override fun onBindViewHolder(holder: MemberScoreViewHolder, position: Int) =
        holder.bind(getItem(position))

}
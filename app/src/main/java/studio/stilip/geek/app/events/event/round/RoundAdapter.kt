package studio.stilip.geek.app.events.event.round

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardRoundBinding
import studio.stilip.geek.domain.entities.Round
import studio.stilip.geek.domain.entities.Score

class RoundAdapter(
    private val onMemberChanged: (Round, Score) -> Unit,
    private val onScoreChanged: (Round, Score) -> Unit,
    private val onAddMemberClicked: (Round) -> Unit,
) : ListAdapter<Round, RoundViewHolder>(RoundDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RoundViewHolder(
            CardRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onMemberChanged,
            onScoreChanged,
            onAddMemberClicked
        )

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) =
        holder.bind(getItem(position))
}
package studio.stilip.geek.app.events.event.round.score

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.Score

object ScoreDiffCallback : DiffUtil.ItemCallback<Score>() {

    override fun areItemsTheSame(oldItem: Score, newItem: Score) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Score, newItem: Score) =
        oldItem == newItem
}
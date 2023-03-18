package studio.stilip.geek.app.events.event.round

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.Round

object RoundDiffCallback : DiffUtil.ItemCallback<Round>() {

    override fun areItemsTheSame(oldItem: Round, newItem: Round) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Round, newItem: Round) =
        oldItem == newItem
}
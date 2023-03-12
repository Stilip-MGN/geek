package studio.stilip.geek.app.events

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.Event

object EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event) =
        oldItem == newItem
}
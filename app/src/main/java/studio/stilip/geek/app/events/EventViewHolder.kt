package studio.stilip.geek.app.events

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardEventBinding
import studio.stilip.geek.domain.entities.Event

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onItemClicked: (Event) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var event: Event

    init {
        itemView.setOnClickListener {
            onItemClicked(event)
        }
    }

    fun bind(item: Event) = with(binding) {
        event = item

        eventName.text = event.eventName
        gameName.text = event.gameName
        place.text = event.place
        date.text = event.date
    }
}
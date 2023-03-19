package studio.stilip.geek.app.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardEventBinding
import studio.stilip.geek.domain.entities.Event

class EventAdapter(
    private val onItemClicked: (Event) -> Unit,
) : ListAdapter<Event, EventViewHolder>(EventDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventViewHolder(
            CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) =
        holder.bind(getItem(position))
}
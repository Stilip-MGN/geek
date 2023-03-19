package studio.stilip.geek.app.events.event_visitor.round

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.events.event.round.RoundDiffCallback
import studio.stilip.geek.databinding.CardRoundVisitorBinding
import studio.stilip.geek.domain.entities.Round

class RoundVisitorAdapter : ListAdapter<Round, RoundVisitorViewHolder>(RoundDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RoundVisitorViewHolder(
            CardRoundVisitorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    override fun onBindViewHolder(holder: RoundVisitorViewHolder, position: Int) =
        holder.bind(getItem(position))
}
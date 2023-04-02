package studio.stilip.geek.app.profile.collection.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.games.GameDiffCallback
import studio.stilip.geek.databinding.CardGameSelectBinding
import studio.stilip.geek.domain.entities.Game

class GameSelectAdapter(
    private val onItemClicked: (String, Boolean) -> Unit,
) : ListAdapter<Game, GameSelectViewHolder>(GameDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GameSelectViewHolder(
            CardGameSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: GameSelectViewHolder, position: Int) =
        holder.bind(getItem(position))
}
package studio.stilip.geek.app.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.games.GameDiffCallback
import studio.stilip.geek.databinding.CardGameHorizontalBinding
import studio.stilip.geek.domain.entities.Game

class GameHorizontalAdapter(
    private val onItemClicked: (String) -> Unit,
) : ListAdapter<Game, GameHorizontalViewHolder>(GameDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GameHorizontalViewHolder(
            CardGameHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: GameHorizontalViewHolder, position: Int) =
        holder.bind(getItem(position))
}
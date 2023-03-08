package studio.stilip.geek.app.games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardGameBinding
import studio.stilip.geek.domain.entities.Game

class GameAdapter(
    private val onItemClicked: (String) -> Unit,
) : ListAdapter<Game, GameViewHolder>(GameDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GameViewHolder(
            CardGameBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) =
        holder.bind(getItem(position))
}
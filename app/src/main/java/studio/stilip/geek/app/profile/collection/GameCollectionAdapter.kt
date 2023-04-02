package studio.stilip.geek.app.profile.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.games.GameDiffCallback
import studio.stilip.geek.databinding.CardGameDelBinding
import studio.stilip.geek.domain.entities.Game

class GameCollectionAdapter(
    private val onItemClicked: (String) -> Unit,
    private val onCloseClicked: (String) -> Unit,
) : ListAdapter<Game, GameCollectionViewHolder>(GameDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GameCollectionViewHolder(
            CardGameDelBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked,
            onCloseClicked
        )

    override fun onBindViewHolder(holder: GameCollectionViewHolder, position: Int) =
        holder.bind(getItem(position))
}
package studio.stilip.geek.app.profile.collection

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardGameDelBinding
import studio.stilip.geek.domain.entities.Game

class GameCollectionViewHolder(
    private val binding: CardGameDelBinding,
    private val onItemClicked: (String) -> Unit,
    private val onCloseClicked: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var game: Game

    init {
        itemView.setOnClickListener {
            onItemClicked(game.id)
        }
    }

    fun bind(item: Game) = with(binding) {
        game = item

        Glide.with(logo)
            .load(game.logo)
            .centerCrop()
            .into(logo)

        name.text = game.name
        time.text = game.time
        players.text = game.countPlayers
        age.text = game.age
        description.text = game.description

        btnClose.setOnClickListener {
            onCloseClicked(game.id)
        }
    }
}
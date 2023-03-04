package studio.stilip.geek.app.games

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardGameBinding
import studio.stilip.geek.domain.entities.Game

class GameViewHolder(
    private val binding: CardGameBinding,
    private val onItemClicked: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var game: Game

    init {
        itemView.setOnClickListener {
            onItemClicked()
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

    }
}
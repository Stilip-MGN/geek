package studio.stilip.geek.app.profile.collection.add

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardGameSelectBinding
import studio.stilip.geek.domain.entities.Game

class GameSelectViewHolder(
    private val binding: CardGameSelectBinding,
    private val onItemClicked: (String, Boolean) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var game: Game
    private var isChecked = false

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

        itemView.setOnClickListener {
            isChecked = isChecked.not()
            checkBox.isChecked = isChecked
            onItemClicked(game.id, isChecked)
        }

    }
}
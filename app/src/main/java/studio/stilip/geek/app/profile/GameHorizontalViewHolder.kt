package studio.stilip.geek.app.profile

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardGameHorizontalBinding
import studio.stilip.geek.domain.entities.Game

class GameHorizontalViewHolder(
    private val binding: CardGameHorizontalBinding,
    private val onItemClicked: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var game: Game

    init {
        itemView.setOnClickListener {
            onItemClicked(game.id)
        }
    }

    fun bind(item: Game): Unit = with(binding) {
        game = item

        Glide.with(logo)
            .load(game.logo)
            .centerCrop()
            .into(logo)
    }
}
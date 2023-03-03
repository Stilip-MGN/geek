package studio.stilip.geek.app.games

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.Game

object GameDiffCallback : DiffUtil.ItemCallback<Game>() {

    override fun areItemsTheSame(oldItem: Game, newItem: Game) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Game, newItem: Game) =
        oldItem == newItem
}
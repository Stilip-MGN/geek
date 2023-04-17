package studio.stilip.geek.app.events.set

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.UserWithScore

object UserWithScoreDiffCallback : DiffUtil.ItemCallback<UserWithScore>() {

    override fun areItemsTheSame(oldItem: UserWithScore, newItem: UserWithScore) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserWithScore, newItem: UserWithScore) =
        oldItem == newItem
}
package studio.stilip.geek.app.events.event

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.User

object MemberDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User) =
        oldItem == newItem
}
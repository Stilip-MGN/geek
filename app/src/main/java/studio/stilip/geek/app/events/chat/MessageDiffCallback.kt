package studio.stilip.geek.app.events.chat

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.Message

object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Message, newItem: Message) =
        oldItem == newItem
}
package studio.stilip.geek.app.events.chat

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardChatBinding
import studio.stilip.geek.domain.entities.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(
    private val binding: CardChatBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var message: Message

    fun bind(item: Message) = with(binding) {
        message = item

        textMessage.text = message.text

        val date = Date(message.createdAt)
        val format = SimpleDateFormat("HH:mm dd.MM.yyyy")
        this.time.text = format.format(date)
    }
}
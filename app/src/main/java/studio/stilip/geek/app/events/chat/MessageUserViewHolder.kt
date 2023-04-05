package studio.stilip.geek.app.events.chat

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardChatUserBinding
import studio.stilip.geek.domain.entities.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageUserViewHolder(
    private val binding: CardChatUserBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var message: Message

    fun bind(item: Message) = with(binding) {
        message = item

        textMessage.text = message.text

        val date = Date(message.createdAt)
        val format = SimpleDateFormat("HH:mm dd.MM.yy")
        this.time.text = format.format(date)
    }
}
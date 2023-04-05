package studio.stilip.geek.app.events.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardChatBinding
import studio.stilip.geek.databinding.CardChatUserBinding
import studio.stilip.geek.domain.entities.Message

class MessageAdapter(
    private val userId: String,
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).createdBy) {
            userId -> 1
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> MessageViewHolder(
                CardChatBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
            else -> MessageUserViewHolder(
                CardChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as MessageViewHolder).bind(getItem(position))
            else -> (holder as MessageUserViewHolder).bind(getItem(position))
        }
    }
}
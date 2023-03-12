package studio.stilip.geek.app.events.event

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardMemberBinding
import studio.stilip.geek.domain.entities.User

class MemberViewHolder(
    private val binding: CardMemberBinding,
    private val onItemClicked: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var member: User

    init {
        itemView.setOnClickListener {
            onItemClicked(member.id)
        }
    }

    fun bind(item: User) = with(binding) {
        member = item

        Glide.with(avatar)
            .load(member.avatar)
            .centerCrop()
            .into(avatar)

        name.text = member.name
    }
}
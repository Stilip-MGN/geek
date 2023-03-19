package studio.stilip.geek.app.events.edit

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import studio.stilip.geek.databinding.CardMemberWithDeleteBinding
import studio.stilip.geek.domain.entities.User

class MemberWithDeleteViewHolder(
    private val binding: CardMemberWithDeleteBinding,
    private val onDeleteMemberClicked: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var member: User

    fun bind(item: User) = with(binding) {
        member = item

        Glide.with(avatar)
            .load(member.avatar)
            .centerCrop()
            .into(avatar)

        name.text = member.name

        btnClose.setOnClickListener {
            onDeleteMemberClicked(member.id)
        }
    }
}
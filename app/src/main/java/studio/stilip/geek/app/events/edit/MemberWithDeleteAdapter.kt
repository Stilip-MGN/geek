package studio.stilip.geek.app.events.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.app.events.event.MemberDiffCallback
import studio.stilip.geek.databinding.CardMemberWithDeleteBinding
import studio.stilip.geek.domain.entities.User

class MemberWithDeleteAdapter(
    private val onDeleteMemberClicked: (String) -> Unit,
) : ListAdapter<User, MemberWithDeleteViewHolder>(MemberDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemberWithDeleteViewHolder(
            CardMemberWithDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onDeleteMemberClicked
        )

    override fun onBindViewHolder(holder: MemberWithDeleteViewHolder, position: Int) =
        holder.bind(getItem(position))
}
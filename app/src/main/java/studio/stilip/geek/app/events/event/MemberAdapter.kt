package studio.stilip.geek.app.events.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardMemberBinding
import studio.stilip.geek.domain.entities.User

class MemberAdapter(
    private val onItemClicked: (String) -> Unit,
) : ListAdapter<User, MemberViewHolder>(MemberDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemberViewHolder(
            CardMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) =
        holder.bind(getItem(position))
}
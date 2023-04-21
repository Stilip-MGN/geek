package studio.stilip.geek.app.events.set

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardMemberScoreVisitorBinding
import studio.stilip.geek.domain.entities.UserWithScore

class UserWithScoreAdapter(
    private val onItemClicked: (UserWithScore) -> Unit,
) : ListAdapter<UserWithScore, UserWithScoreViewHolder>(UserWithScoreDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserWithScoreViewHolder(
            CardMemberScoreVisitorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked,
        )

    override fun onBindViewHolder(holder: UserWithScoreViewHolder, position: Int) =
        holder.bind(getItem(position))

}
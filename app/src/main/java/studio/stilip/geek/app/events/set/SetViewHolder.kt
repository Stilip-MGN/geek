package studio.stilip.geek.app.events.set

import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardRoundVisitorBinding
import studio.stilip.geek.domain.entities.SetWithList

class SetViewHolder(
    private val binding: CardRoundVisitorBinding,
    private val onClickedItem: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var set: SetWithList

    init {
        itemView.setOnClickListener {
            onClickedItem(set.id)
        }
    }

    fun bind(item: SetWithList) = with(binding) {
        set = item

        title.text = set.title
        val adapter = UserWithScoreAdapter { onClickedItem(set.id) }

        adapter.submitList(set.membersScores)

        with(binding) {
            recScore.adapter = adapter
        }
    }
}
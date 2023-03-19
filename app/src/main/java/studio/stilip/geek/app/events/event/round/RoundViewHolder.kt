package studio.stilip.geek.app.events.event.round

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.R
import studio.stilip.geek.app.events.event.round.score.MemberScoreAdapter
import studio.stilip.geek.databinding.CardRoundBinding
import studio.stilip.geek.domain.entities.Round
import studio.stilip.geek.domain.entities.Score

class RoundViewHolder(
    private val binding: CardRoundBinding,
    private val onMemberChanged: (Round, Score) -> Unit,
    private val onScoreChanged: (Round, Score) -> Unit,
    private val onAddMemberClicked: (Round) -> Unit,
    private val onDeleteClicked: (Round) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var round: Round

    fun bind(item: Round) = with(binding) {
        round = item

        title.text = round.title
        val adapter = MemberScoreAdapter({ score ->
            onMemberChanged(round, score)
        }, { score ->
            onScoreChanged(round, score)
        })
        adapter.submitList(round.scores)
        with(binding) {
            recScore.adapter = adapter
            btnAddPlayer.setOnClickListener {
                onAddMemberClicked(round)
            }
            btnDelete.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setTitle(R.string.sure_want_to_delete)
                    .setPositiveButton(R.string.accept) { dialog, _ ->
                        onDeleteClicked(round)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
    }
}
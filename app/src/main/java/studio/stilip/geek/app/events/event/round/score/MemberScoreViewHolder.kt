package studio.stilip.geek.app.events.event.round.score

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import studio.stilip.geek.databinding.CardMemberScoreBinding
import studio.stilip.geek.domain.entities.Score
import studio.stilip.geek.domain.entities.User

class MemberScoreViewHolder(
    private val context: Context,
    private val binding: CardMemberScoreBinding,
    private val onMemberChanged: (Score) -> Unit,
    private val onScoreChanged: (Score) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var scoreItem: Score

    fun bind(item: Score) = with(binding) {
        scoreItem = item

        val adapterPlayer = PlayerSpinnerAdapter(context)
        spinnerPlayer.adapter = adapterPlayer
        adapterPlayer.addAll(scoreItem.members)

        if (scoreItem.members.isNotEmpty()) {
            val member = scoreItem.members.first { m -> m.id == scoreItem.memberId }
            with(binding) {
                spinnerPlayer.setSelection(scoreItem.members.lastIndexOf(member))
            }
        }
        val timer = object : CountDownTimer(2500, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                onScoreChanged(scoreItem)
            }
        }

        score.setText(scoreItem.score.toString())
        score.doAfterTextChanged { edit ->
            timer.cancel()
            timer.start()

            val newScore = if (edit.toString().isEmpty()) 0 else edit.toString().toInt()
            scoreItem = scoreItem.copy(score = newScore)
        }

        spinnerPlayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newUser = spinnerPlayer.selectedItem as User
                scoreItem = scoreItem.copy(memberId = newUser.id)
                onMemberChanged(scoreItem)
            }
        }

    }
}
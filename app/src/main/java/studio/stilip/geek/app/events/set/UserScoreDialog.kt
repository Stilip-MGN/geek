package studio.stilip.geek.app.events.set

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import studio.stilip.geek.app.events.event.round.score.PlayerSpinnerAdapter
import studio.stilip.geek.databinding.DialogUserScoreBinding
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.entities.UserWithScore

class UserScoreDialog(
    private val userWithScore: UserWithScore,
    private val members: List<User>,
    private val onSaveClickListener: (UserWithScore) -> Unit,
    private val onDeleteClickListener: (UserWithScore) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogUserScoreBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }

            btnSave.setOnClickListener {
                val user = spinnerPlayer.selectedItem as User
                val res = UserWithScore(userWithScore.id, user, score.text.toString().toInt())
                onSaveClickListener(res)
                dismiss()
            }

            btnDelete.setOnClickListener {
                onDeleteClickListener(userWithScore)
                dismiss()
            }

            score.setText(userWithScore.score.toString())

            val adapterPlayer = PlayerSpinnerAdapter(this@UserScoreDialog.requireContext())
            spinnerPlayer.adapter = adapterPlayer
            adapterPlayer.addAll(members)

            val member = userWithScore.user
            with(binding) {
                spinnerPlayer.setSelection(members.lastIndexOf(member))
            }
        }

        return builder.create()
    }
}
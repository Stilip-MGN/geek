package studio.stilip.geek.app.events.event

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import studio.stilip.geek.databinding.DialogRoundBinding

class RoundDialog(
    private val onSaveClickListener: (String) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogRoundBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }

            btnSave.setOnClickListener {
                onSaveClickListener(editTitle.text.toString())
                dismiss()
            }
        }

        return builder.create()

    }
}
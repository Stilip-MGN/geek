package studio.stilip.geek.app.events.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.hideKeyboard
import studio.stilip.geek.databinding.FragmentEventChatBinding
import studio.stilip.geek.domain.entities.Message

@AndroidEntryPoint
class EventChatFragment : Fragment(R.layout.fragment_event_chat) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventChatViewModel by viewModels()

    val messages = arrayListOf(
        Message("1", 3818394L, "", "Раз два три"),
        Message("2", 3818398L, "", "Ич ни сан")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEventChatBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        // hostViewModel.setToolbarTitle(getText(R.string.title_events).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MessageAdapter(viewModel.userId)

        with(binding) {
            recMessages.adapter = adapter

            adapter.submitList(messages)

            btnSend.setOnClickListener {
                val mes = editMessage.text.toString()
                messages.add(Message("3", 3818399L, viewModel.userId, mes))
                adapter.submitList(messages)
                editMessage.setText("")
                this@EventChatFragment.hideKeyboard()
            }
        }

    }
}
package studio.stilip.geek.app.events.chat

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.hideKeyboard
import studio.stilip.geek.databinding.FragmentEventChatBinding

@AndroidEntryPoint
class EventChatFragment : Fragment(R.layout.fragment_event_chat) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEventChatBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.title_event).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MessageAdapter(viewModel.userId)

        with(binding) {
            recMessages.adapter = adapter

            editMessage.doOnTextChanged { text, _, _, _ ->
                viewModel.onMessageChanged(text.toString())
            }

            btnSend.setOnClickListener {
                viewModel.onSendClicked()
                editMessage.setText("")
                this@EventChatFragment.hideKeyboard()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { messages ->
                    adapter.submitList(messages)
                }
        }

    }
}
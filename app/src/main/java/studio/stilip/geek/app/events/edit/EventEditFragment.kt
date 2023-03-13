package studio.stilip.geek.app.events.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.MemberAdapter
import studio.stilip.geek.databinding.FragmentEventEditBinding

@AndroidEntryPoint
class EventEditFragment : Fragment(R.layout.fragment_event_edit) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEventEditBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.title_event).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MemberAdapter {
            //TODO Страница пользователей
        }

        with(binding) {
            recMembers.adapter = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { event ->
                    with(binding) {
                        editEventName.setText(event.eventName)
                        editPlace.setText(event.place)
                        editDescription.setText(event.description)
                        editDate.setText(event.date)
                        editMembersCount.setText(event.maxMembers.toString())
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.members
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { members ->
                    adapter.submitList(members)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.game
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { game ->
                    with(binding) {
                        gameName.text = game.name

                        Glide.with(gameLogo)
                            .load(game.logo)
                            .centerCrop()
                            .into(gameLogo)
                    }
                }
        }
    }
}
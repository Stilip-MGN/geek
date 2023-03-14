package studio.stilip.geek.app.events

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.EventFragment.Companion.EVENT_ID
import studio.stilip.geek.databinding.FragmentEventsBinding

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEventsBinding.bind(view)

        hostViewModel.setBottomBarVisible(true)
        hostViewModel.setToolbarTitle(getText(R.string.title_events).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        val adapter = EventAdapter { id ->
            val arg = Bundle().apply {
                putString(EVENT_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_events_to_event,
                arg
            )
        }

        with(binding) {
            recEvents.adapter = adapter

            btnAdd.setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_events_to_event_create
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { events ->
                    adapter.submitList(events)
                }
        }
    }

}
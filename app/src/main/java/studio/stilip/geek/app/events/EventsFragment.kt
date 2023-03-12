package studio.stilip.geek.app.events

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
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

        val adapter = EventAdapter {}

        with(binding) {
            recEvents.adapter = adapter
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
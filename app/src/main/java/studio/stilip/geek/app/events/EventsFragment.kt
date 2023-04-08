package studio.stilip.geek.app.events

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        val adapter = EventAdapter { event ->
            val arg = Bundle().apply {
                putString(EVENT_ID, event.id)
            }
            if (event.ownId == viewModel.userId)
                findNavController().navigate(
                    R.id.action_navigation_events_to_event,
                    arg
                )
            else
                findNavController().navigate(
                    R.id.action_navigation_events_to_event_visitor,
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
            val multiItems = arrayOf(
                getString(R.string.i_organizer),
                getString(R.string.i_member),
                getString(R.string.i_not_member),
                getString(R.string.actual),
                getString(R.string.not_actual)
            )

            btnFilter.setOnClickListener {
                val checkedItems = viewModel.checkedItems.value.clone()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.filter))
                    .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(getString(R.string.accept)) { _, _ ->
                        viewModel.onCheckSaved(checkedItems)
                    }
                    .setMultiChoiceItems(multiItems, checkedItems) { _, _, _ -> }
                    .show()
            }

            search.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    println(query)
                    if (query.isNullOrEmpty())
                        return false
                    viewModel.onQuerySubmitChanged(query)
                    search.clearFocus()
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query.isNullOrEmpty())
                        viewModel.onQuerySubmitChanged("")
                    return false
                }
            })
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
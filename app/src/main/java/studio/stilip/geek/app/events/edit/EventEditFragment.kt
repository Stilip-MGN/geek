package studio.stilip.geek.app.events.edit

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.app.events.event.MemberAdapter
import studio.stilip.geek.databinding.FragmentEventEditBinding
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Game

@AndroidEntryPoint
class EventEditFragment : Fragment(R.layout.fragment_event_edit) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventEditViewModel by viewModels()

    lateinit var binding: FragmentEventEditBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context as AppCompatActivity

        context.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_done_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_done -> {
                        viewModel.update(collectNewEvent())
                        val arg = Bundle().apply {
                            putString(EventFragment.EVENT_ID, viewModel.eventId)
                        }
                        findNavController().navigate(
                            R.id.action_navigation_event_edit_to_event,
                            arg
                        )
                        true
                    }
                    R.id.menu_delete -> {
                        viewModel.delete()
                        findNavController().navigate(
                            R.id.action_navigation_event_edit_to_events,
                        )
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEventEditBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.title_event).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MemberAdapter {
            //TODO Страница пользователей
        }

        val adapterGames = GameSpinnerAdapter(this@EventEditFragment.context!!)

        with(binding) {
            recMembers.adapter = adapter
            spinner.adapter = adapterGames
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
            viewModel.event.combine(viewModel.games) { event, games ->
                event to games
            }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { (event, games) ->
                    if (games.isNotEmpty()) {
                        adapterGames.addAll(games)
                        val game = games.first { g -> g.id == event.gameId }
                        with(binding) {
                            spinner.setSelection(games.lastIndexOf(game))
                        }
                    }
                }

        }
    }

    private fun collectNewEvent(): Event {
        with(binding) {
            val id = viewModel.eventId
            val eventName = editEventName.text.toString()
            val gameId = (spinner.selectedItem as Game).id
            val gameName = (spinner.selectedItem as Game).name
            val place = editPlace.text.toString()
            val description = editDescription.text.toString()
            val date = editDate.text.toString()
            val maxMembers = editMembersCount.text.toString().toInt()

            return Event(
                id = id,
                eventName = eventName,
                gameId = gameId,
                gameName = gameName,
                place = place,
                date = date,
                description = description,
                maxMembers = maxMembers
            )
        }
    }

}
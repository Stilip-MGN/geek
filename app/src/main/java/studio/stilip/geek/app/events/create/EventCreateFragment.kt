package studio.stilip.geek.app.events.create

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
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.edit.GameSpinnerAdapter
import studio.stilip.geek.databinding.FragmentEventCreateBinding
import studio.stilip.geek.domain.entities.Event
import studio.stilip.geek.domain.entities.Game

@AndroidEntryPoint
class EventCreateFragment : Fragment(R.layout.fragment_event_create) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventCreateViewModel by viewModels()

    lateinit var binding: FragmentEventCreateBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context as AppCompatActivity

        context.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.done_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_done -> {
                        viewModel.create(collectNewEvent())
                        findNavController().navigate(
                            R.id.action_navigation_event_create_to_events
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

        binding = FragmentEventCreateBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.create).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapterGames = GameSpinnerAdapter(this@EventCreateFragment.context!!)

        with(binding) {
            spinner.adapter = adapterGames
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.games
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { games ->
                    if (games.isNotEmpty()) {
                        adapterGames.addAll(games)
                    }
                }

        }
    }

    private fun collectNewEvent(): Event {
        with(binding) {
            val eventName = editEventName.text.toString()
            val gameId = (spinner.selectedItem as Game).id
            val gameName = (spinner.selectedItem as Game).name
            val place = editPlace.text.toString()
            val description = editDescription.text.toString()
            val date = editDate.text.toString()
            val maxMembers = editMembersCount.text.toString().toInt()

            return Event(
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
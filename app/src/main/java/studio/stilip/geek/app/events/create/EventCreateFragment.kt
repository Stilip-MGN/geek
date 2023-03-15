package studio.stilip.geek.app.events.create

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
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
                        viewModel.onCompleteClicked()
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

            editEventName.doOnTextChanged { text, _, _, _ ->
                viewModel.onEventNameChanged(text.toString())
            }

            editDate.doOnTextChanged { text, _, _, _ ->
                viewModel.onDateChanged(text.toString())
            }

            editDescription.doOnTextChanged { text, _, _, _ ->
                viewModel.onDescriptionChanged(text.toString())
            }

            editPlace.doOnTextChanged { text, _, _, _ ->
                viewModel.onPlaceChanged(text.toString())
            }

            editMembersCount.doOnTextChanged { text, _, _, _ ->
                viewModel.onMaxMembersChanged(text.toString())
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onGameChanged(spinner.selectedItem as Game)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.eventNameHelper
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { res ->
                        editEventNameLayout.helperText = getText(res)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventCreated
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { isSuccess ->
                    when (isSuccess) {
                        true -> {
                            findNavController().navigate(
                                R.id.action_navigation_event_create_to_events
                            )
                        }
                        false -> {}
                        null -> {}
                    }
                }
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

}
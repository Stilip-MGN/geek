package studio.stilip.geek.app.events.event_visitor

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.app.events.event.MemberAdapter
import studio.stilip.geek.app.events.event.RoundSpinnerAdapter
import studio.stilip.geek.app.events.set.SetAdapter
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment
import studio.stilip.geek.databinding.FragmentEventVisitorBinding
import studio.stilip.geek.domain.entities.Round

@AndroidEntryPoint
class EventVisitorFragment : Fragment(R.layout.fragment_event_visitor) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventVisitorViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context as AppCompatActivity

        context.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.chat_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_chat -> {
                        val arg = Bundle().apply {
                            putString(EventFragment.EVENT_ID, viewModel.eventId)
                        }
                        findNavController().navigate(
                            R.id.action_navigation_event_visitor_to_event_chat,
                            arg
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

        val binding = FragmentEventVisitorBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.title_event).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MemberAdapter {
            //TODO Страница пользователей
        }
        val roundAdapter = RoundSpinnerAdapter(this.requireContext())
        val setAdapter = SetAdapter { }
        var countMembers = 0
        var isUserSubscribed = false
        var isCanChangeMemberStatus = false

        with(binding) {
            recMembers.adapter = adapter
            recSets.adapter = setAdapter
            spRounds.adapter = roundAdapter

            btnUnsub.setOnClickListener {
                viewModel.onUnsubscribeClick()
            }

            btnSub.setOnClickListener {
                viewModel.onSubscribeClick()
            }

            spRounds.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onRoundChanged(spRounds.selectedItem as Round)
                }
            }
        }

        fun updateUI() {
            with(binding) {
                if (isCanChangeMemberStatus) {
                    spRounds.visibility = View.GONE
                    if (isUserSubscribed) {
                        btnSub.visibility = View.GONE
                        btnUnsub.visibility = View.VISIBLE
                    } else {
                        btnSub.visibility = View.VISIBLE
                        btnUnsub.visibility = View.GONE
                    }
                } else {
                    spRounds.visibility = View.VISIBLE
                    btnSub.visibility = View.GONE
                    btnUnsub.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rounds
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { rounds ->
                    isCanChangeMemberStatus = rounds.isEmpty()
                    updateUI()
                    roundAdapter.clear()
                    roundAdapter.addAll(rounds)

                    if (rounds.isNotEmpty())
                        binding.spRounds.setSelection(rounds.size - 1)

                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sets
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { sets ->
                    setAdapter.submitList(sets)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { event ->
                    with(binding) {
                        eventName.text = event.eventName
                        place.text = event.place
                        description.text = event.description
                        date.text = event.date
                        countMembers = event.maxMembers
                        membersCount.text = "0/${countMembers}"
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.members
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { members ->
                    isUserSubscribed = members.any { user -> user.id == viewModel.userId }
                    updateUI()
                    adapter.submitList(members)
                    with(binding) {
                        membersCount.text = "${members.count()}/${countMembers}"
                    }
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

                        fun navigateToGameInformation() {
                            val arg = Bundle().apply {
                                putString(GameInfoFragment.GAME_ID, game.id)
                            }
                            findNavController().navigate(
                                R.id.action_navigation_event_visitor_to_game_info,
                                arg
                            )
                        }

                        gameName.setOnClickListener {
                            navigateToGameInformation()
                        }

                        gameLogo.setOnClickListener {
                            navigateToGameInformation()
                        }
                    }
                }
        }
    }

    companion object {
        const val EVENT_ID = "event_id"
    }
}
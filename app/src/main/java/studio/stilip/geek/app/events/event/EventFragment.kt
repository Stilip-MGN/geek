package studio.stilip.geek.app.events.event

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.round.RoundAdapter
import studio.stilip.geek.databinding.FragmentEventBinding

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_event) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: EventViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context as AppCompatActivity

        context.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        val arg = Bundle().apply {
                            putString(EVENT_ID, viewModel.eventId)
                        }
                        findNavController().navigate(
                            R.id.action_navigation_event_to_event_edit,
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

        val binding = FragmentEventBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.title_event).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = MemberAdapter {
            //TODO Страница пользователей
        }
        var countMembers = 0

        val roundAdapter = RoundAdapter({ round, score ->
            viewModel.onMemberChanged(round.id, score.id, score.memberId)
        }, { round, score ->
            viewModel.onScoreChanged(round.id, score.id, score.score)
        }, { round ->
            viewModel.onAddMemberClicked(round.id)
        })

        with(binding) {
            recMembers.adapter = adapter
            recRounds.adapter = roundAdapter

            btnUnsub.setOnClickListener {
                viewModel.onUnsubscribeClick()
            }
            btnAddRound.setOnClickListener {
                RoundDialog { title ->
                    viewModel.onAddRoundClicked(title)
                }.show(parentFragmentManager, "dialog")
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rounds
                .combine(viewModel.members) { rounds, members ->
                    rounds to members
                }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { (rounds, members) ->
                    roundAdapter.submitList(rounds.map { r ->
                        with(binding) {
                            if (rounds.isNotEmpty()) {
                                btnSub.visibility = View.GONE
                                btnUnsub.visibility = View.GONE
                            } else {
                                if (members.firstOrNull { member -> member.id == viewModel.userId } != null) {
                                    btnSub.visibility = View.GONE
                                    btnUnsub.visibility = View.VISIBLE
                                } else {
                                    btnUnsub.visibility = View.GONE
                                    btnSub.visibility = View.VISIBLE
                                }
                            }
                        }
                        r.copy(scores = r.scores.map { s ->
                            s.copy(members = members)
                        })
                    })
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
                    adapter.submitList(members)
                    with(binding) {
                        membersCount.text = "${members.count()}/${countMembers}"

                        if (members.firstOrNull { member -> member.id == viewModel.userId } != null) {
                            btnSub.visibility = View.GONE
                            btnUnsub.visibility = View.VISIBLE
                        } else {
                            btnUnsub.visibility = View.GONE
                            btnSub.visibility = View.VISIBLE
                        }
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
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect {
                    with(binding) {
                        btnSub.setOnClickListener {
                            viewModel.onSubscribeClick()
                        }
                    }
                }
        }
    }

    companion object {
        const val EVENT_ID = "event_id"
    }
}
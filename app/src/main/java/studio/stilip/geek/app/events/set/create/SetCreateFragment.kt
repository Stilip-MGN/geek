package studio.stilip.geek.app.events.set.create

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.app.events.set.UserScoreDialog
import studio.stilip.geek.app.events.set.UserWithScoreAdapter
import studio.stilip.geek.databinding.FragmentSetEditBinding

@AndroidEntryPoint
class SetCreateFragment : Fragment(R.layout.fragment_set_edit) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: SetCreateViewModel by viewModels()

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

        val binding = FragmentSetEditBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle("Создание партии")
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = UserWithScoreAdapter {
            UserScoreDialog(it, viewModel.members.value, { us ->
                viewModel.onDialogSavedChanges(us)
            }, { us ->
                viewModel.onDialogDeleted(us)
            }).show(parentFragmentManager, "dialog")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.membersScores
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { list ->
                    adapter.submitList(list)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setCreated
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { isSuccess ->
                    when (isSuccess) {
                        true -> {
                            val arg = Bundle().apply {
                                putString(EventFragment.EVENT_ID, viewModel.eventId)
                            }
                            findNavController().navigate(
                                R.id.action_navigation_set_create_to_event,
                                arg
                            )
                        }
                        false -> {}
                        null -> {}
                    }
                }
        }

        with(binding) {
            recMemberScore.adapter = adapter

            btnAddPlayer.setOnClickListener {
                viewModel.onAddButtonClicked()
            }

            editSetName.doOnTextChanged { text, _, _, _ ->
                viewModel.onTitleChanged(text.toString())
            }
        }
    }

}
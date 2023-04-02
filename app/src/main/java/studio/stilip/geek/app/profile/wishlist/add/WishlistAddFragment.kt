package studio.stilip.geek.app.profile.wishlist.add

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
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
import studio.stilip.geek.app.profile.collection.add.GameSelectAdapter
import studio.stilip.geek.databinding.FragmentCollectionAddBinding

@AndroidEntryPoint
class WishlistAddFragment : Fragment(R.layout.fragment_collection_add) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: WishlistAddViewModel by viewModels()

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

        val binding = FragmentCollectionAddBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getString(R.string.adding_title))
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = GameSelectAdapter { id, isChecked ->
            viewModel.onGameClicked(id, isChecked)
        }

        with(binding) {
            recGames.adapter = adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.games
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gamesAdded
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { isSuccess ->
                    when (isSuccess) {
                        true -> {
                            findNavController().navigate(
                                R.id.action_navigation_wishlist_add_to_wishlist
                            )
                        }
                        false -> {}
                        null -> {}
                    }
                }
        }
    }
}
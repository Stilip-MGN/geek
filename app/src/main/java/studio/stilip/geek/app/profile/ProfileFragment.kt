package studio.stilip.geek.app.profile

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
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment.Companion.GAME_ID
import studio.stilip.geek.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: ProfileViewModel by viewModels()

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
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view)

        hostViewModel.setBottomBarVisible(true)
        hostViewModel.setToolbarTitle(getText(R.string.profile_title).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        val colAdapter = GameHorizontalAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_profile_to_game_info,
                arg
            )
        }
        val wishlistAdapter = GameHorizontalAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_profile_to_game_info,
                arg
            )
        }

        with(binding) {
            recMyGames.adapter = colAdapter
            recWishlist.adapter = wishlistAdapter

            btnCollection.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profile_to_collection)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { user ->
                    with(binding) {
                        name.text = user.name
                        status.text = user.status

                        Glide.with(avatar)
                            .load(user.avatar)
                            .centerCrop()
                            .into(avatar)
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.collection
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    colAdapter.submitList(games)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wishlist
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    wishlistAdapter.submitList(games)
                }
        }

    }
}
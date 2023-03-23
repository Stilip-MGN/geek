package studio.stilip.geek.app.profile.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.games.GameAdapter
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment
import studio.stilip.geek.databinding.FragmentWishlistBinding

@AndroidEntryPoint
class WishlistFragment : Fragment(R.layout.fragment_collection) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: WishlistViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWishlistBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.wishlist).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = GameAdapter { id ->
            val arg = Bundle().apply {
                putString(GameInfoFragment.GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_wishlist_to_game_info,
                arg
            )
        }

        with(binding) {
            recGames.adapter = adapter

            btnAdd.setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_wishlist_to_wishlist_add
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wishlist
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }
    }
}
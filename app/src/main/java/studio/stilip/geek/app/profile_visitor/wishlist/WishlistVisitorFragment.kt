package studio.stilip.geek.app.profile_visitor.wishlist

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
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment.Companion.GAME_ID
import studio.stilip.geek.databinding.FragmentWishlistVisitorBinding

@AndroidEntryPoint
class WishlistVisitorFragment : Fragment(R.layout.fragment_wishlist_visitor) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: WishlistVisitorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWishlistVisitorBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.wishlist).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = GameAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_wishlist_to_game_info,
                arg
            )
        }

        binding.recGames.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wishlist
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }
    }
}
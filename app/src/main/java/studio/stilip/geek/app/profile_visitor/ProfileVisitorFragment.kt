package studio.stilip.geek.app.profile_visitor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment.Companion.GAME_ID
import studio.stilip.geek.app.profile.GameHorizontalAdapter
import studio.stilip.geek.databinding.FragmentProfileVisitorBinding

@AndroidEntryPoint
class ProfileVisitorFragment : Fragment(R.layout.fragment_profile_visitor) {
    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: ProfileVisitorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileVisitorBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.profile_title).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val colAdapter = GameHorizontalAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_profile_visitor_to_game_info,
                arg
            )
        }
        val wishlistAdapter = GameHorizontalAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_profile_visitor_to_game_info,
                arg
            )
        }

        with(binding) {
            recMyGames.adapter = colAdapter
            recWishlist.adapter = wishlistAdapter

            btnCollection.setOnClickListener {
                val arg = Bundle().apply {
                    putString(VISITOR_ID, viewModel.user.value.id)
                }
                findNavController().navigate(
                    R.id.action_navigation_profile_visitor_to_collection_visitor,
                    arg
                )
            }

            btnWishlist.setOnClickListener {
                val arg = Bundle().apply {
                    putString(VISITOR_ID, viewModel.user.value.id)
                }
                findNavController().navigate(
                    R.id.action_navigation_profile_visitor_to_wishlist_visitor,
                    arg
                )
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

    companion object {
        const val VISITOR_ID = "visitor_id"
    }
}
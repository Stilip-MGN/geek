package studio.stilip.geek.app.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view)

        hostViewModel.setBottomBarVisible(true)
        hostViewModel.setToolbarTitle(getText(R.string.profile_title).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        val colAdapter = GameHorizontalAdapter {}
        val wishlistAdapter = GameHorizontalAdapter {}

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { user ->
                    with(binding) {
                        name.text = user.name
                        status.text = user.status

                        recMyGames.adapter = colAdapter
                        recWishlist.adapter = wishlistAdapter

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
package studio.stilip.geek.app.games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment.Companion.GAME_ID
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.databinding.FragmentGamesBinding

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGamesBinding.bind(view)

        hostViewModel.setBottomBarVisible(true)
        hostViewModel.setToolbarTitle(getText(R.string.title_games).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        val adapter = GameAdapter { id ->
            val arg = Bundle().apply {
                putString(GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_games_to_game_info,
                arg
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { user ->
                    if (user == null) {
                        findNavController().navigate(R.id.action_navigation_games_to_signIn)
                    } else {
                        UserCacheManager.setUserId(user.uid)
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.games
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }

        with(binding) {
            recGames.adapter = adapter
        }

    }

}
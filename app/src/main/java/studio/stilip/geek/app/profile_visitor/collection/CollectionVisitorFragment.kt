package studio.stilip.geek.app.profile_visitor.collection

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
import studio.stilip.geek.databinding.FragmentCollectionVisitorBinding

@AndroidEntryPoint
class CollectionVisitorFragment : Fragment(R.layout.fragment_collection_visitor) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: CollectionVisitorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCollectionVisitorBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getString(R.string.collection_games))
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = GameAdapter { id ->
            val arg = Bundle().apply {
                putString(GameInfoFragment.GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_collection_visitor_to_game_info,
                arg
            )
        }
        
        binding.recGames.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.collection
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }
    }
}
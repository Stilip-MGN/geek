package studio.stilip.geek.app.profile.collection

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
import studio.stilip.geek.app.games.gameinfo.GameInfoFragment
import studio.stilip.geek.databinding.FragmentCollectionBinding

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: CollectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCollectionBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.my_games_collection).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        val adapter = GameCollectionAdapter({ id ->
            val arg = Bundle().apply {
                putString(GameInfoFragment.GAME_ID, id)
            }
            findNavController().navigate(
                R.id.action_navigation_collection_to_game_info,
                arg
            )
        }, { id ->
            viewModel.onCloseClicked(id)
        })

        with(binding) {
            recGames.adapter = adapter

            btnAdd.setOnClickListener {
                findNavController().navigate(
                    R.id.action_navigation_collection_to_collection_add,
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.collection
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { games ->
                    adapter.submitList(games)
                }
        }
    }
}
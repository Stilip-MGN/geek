package studio.stilip.geek.app.games.gameinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import studio.stilip.geek.databinding.FragmentGameInfoBinding

@AndroidEntryPoint
class GameInfoFragment : Fragment(R.layout.fragment_game_info) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: GameInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGameInfoBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.information_about_game).toString())
        hostViewModel.setToolbarBackBtnVisible(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.game
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { game ->
                    with(binding) {
                        name.text = game.name
                        time.text = game.time
                        age.text = game.age
                        description.text = game.description
                        players.text = game.countPlayers
                        dimensions.text = game.dimensions
                        weight.text = game.weight

                        Glide.with(imgSw)
                            .load(game.logo)
                            .centerCrop()
                            .into(imgSw)
                    }
                }
        }

        binding.rule.setOnClickListener {
            try {
                val link = viewModel.game.value.ruleLink
                val uri = Uri.parse(link)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this.context, R.string.failed_load_rule, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val GAME_ID = "game_id"
    }

}
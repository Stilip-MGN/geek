package studio.stilip.geek.app.games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import studio.stilip.geek.R
import studio.stilip.geek.databinding.FragmentGamesBinding
import studio.stilip.geek.domain.Game

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {

    private val viewModel: GamesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGamesBinding.bind(view)

        val adapter = GameAdapter { }
        adapter.submitList(arr)

        with(binding) {
            recGames.adapter = adapter
        }

    }

    val arr = arrayListOf(
        Game(name = "A", time = "30 min", countPlayers = "2-3", age = "10+"),
        Game(
            name = "B", time = "120 min", countPlayers = "1-3", age = "15+",
            description = "Вышел в поле дед",
            logo = "https://tesera.ru/images/items/1401961,3/200x200xpa/photo1.png"
        ),
    )
}
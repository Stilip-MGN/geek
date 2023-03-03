package studio.stilip.geek.app.games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import studio.stilip.geek.R
import studio.stilip.geek.databinding.FragmentGamesBinding

@AndroidEntryPoint
class GamesFragment : Fragment(R.layout.fragment_games) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGamesBinding.bind(view)
    }
}
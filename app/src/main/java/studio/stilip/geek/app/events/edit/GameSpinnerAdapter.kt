package studio.stilip.geek.app.events.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import studio.stilip.geek.R
import studio.stilip.geek.databinding.SpinnerGameBinding
import studio.stilip.geek.domain.entities.Game

class GameSpinnerAdapter(context: Context) :
    ArrayAdapter<Game>(context, R.layout.spinner_game, arrayListOf()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_game, parent, false)
        val binding = SpinnerGameBinding.bind(view)

        with(binding) {
            val game = getItem(position)!!
            name.text = game.name

            Glide.with(logo)
                .load(game.logo)
                .centerCrop()
                .into(logo)
        }

        return view
    }
}
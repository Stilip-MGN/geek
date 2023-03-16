package studio.stilip.geek.app.events.event.round.score

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import studio.stilip.geek.R
import studio.stilip.geek.databinding.SpinnerPlayerBinding
import studio.stilip.geek.domain.entities.User

class PlayerSpinnerAdapter(
    context: Context
) :
    ArrayAdapter<User>(context, R.layout.spinner_player, arrayListOf()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_player, parent, false)
        val binding = SpinnerPlayerBinding.bind(view)

        with(binding) {
            val player = getItem(position)!!
            name.text = player.name

            Glide.with(avatar)
                .load(player.avatar)
                .centerCrop()
                .into(avatar)
        }

        return view
    }
}
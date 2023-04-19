package studio.stilip.geek.app.events.event.round

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import studio.stilip.geek.R
import studio.stilip.geek.databinding.SpinnerRoundBinding
import studio.stilip.geek.domain.entities.RoundNew

class RoundSpinnerAdapter(
    context: Context
) :
    ArrayAdapter<RoundNew>(context, R.layout.spinner_round, arrayListOf()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_round, parent, false)
        val binding = SpinnerRoundBinding.bind(view)

        with(binding) {
            val round = getItem(position)!!
            title.text = round.title
        }

        return view
    }
}
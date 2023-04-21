package studio.stilip.geek.app.events.set

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import studio.stilip.geek.databinding.CardRoundVisitorBinding
import studio.stilip.geek.domain.entities.*

class SetAdapter(
    private val onClickedItem: (String) -> Unit,
) : ListAdapter<SetWithList, SetViewHolder>(SetDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SetViewHolder(
            CardRoundVisitorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickedItem
        )

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) =
        holder.bind(getItem(position))
}
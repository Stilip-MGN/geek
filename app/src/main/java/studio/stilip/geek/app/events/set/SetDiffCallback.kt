package studio.stilip.geek.app.events.set

import androidx.recyclerview.widget.DiffUtil
import studio.stilip.geek.domain.entities.SetWithList

object SetDiffCallback : DiffUtil.ItemCallback<SetWithList>() {

    override fun areItemsTheSame(oldItem: SetWithList, newItem: SetWithList) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SetWithList, newItem: SetWithList) =
        oldItem == newItem
}
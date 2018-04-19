package im.nfc.nfsee.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class ExpandableViewHolder(val view: View, val expanded: MutableList<Boolean>) : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            val position = it.tag as Int
            expanded[position] = !expanded[position]
            setDetailVisibility(view, expanded[position])
        }
    }

    abstract fun setDetailVisibility(view: View, visibility: Boolean)

    fun bind(position: Int) = with(view) {
        setDetailVisibility(this, expanded[position])
    }
}
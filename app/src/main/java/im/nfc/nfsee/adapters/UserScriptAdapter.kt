package im.nfc.nfsee.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.nfc.nfsee.R
import im.nfc.nfsee.models.UserScript
import kotlinx.android.synthetic.main.script_item.view.*

class UserScriptAdapter(
        private val items: List<UserScript>,
        private val clickHandler: (Int) -> Unit,
        private val longClickHandler: (Int) -> Unit
)
    : RecyclerView.Adapter<UserScriptAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context).inflate(R.layout.script_item, parent, false)

        val holder = ViewHolder(view)

        // it must be executed after the constructor of ViewHolder, in order to override the
        // setting on OnClickListener in ViewHolder
        view.setOnClickListener {
            clickHandler(view.tag as Int)
        }

        view.setOnLongClickListener {
            longClickHandler(view.tag as Int)
            true
        }

        return holder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], position)

    inner class ViewHolder(view: View) : ExpandableViewHolder(view, null) {

        override fun setDetailVisibility(view: View, visibility: Boolean) {}

        fun bind(item: UserScript, position: Int) = with(view) {
            super.bind(position)
            tag = position
            text_script_title.text = item.title
            text_script_content.text = item.script
        }
    }
}
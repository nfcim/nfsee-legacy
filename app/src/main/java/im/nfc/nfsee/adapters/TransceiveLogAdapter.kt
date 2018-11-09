package im.nfc.nfsee.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.nfc.nfsee.R
import im.nfc.nfsee.nfc.TransceiveLog
import kotlinx.android.synthetic.main.log_item.view.*

class TransceiveLogAdapter(private val items: List<TransceiveLog>)
    : RecyclerView.Adapter<TransceiveLogAdapter.ViewHolder>() {

    private val expanded = MutableList(items.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater
            .from(parent.context).inflate(R.layout.log_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], position)

    inner class ViewHolder(view: View) : ExpandableViewHolder(view, expanded) {

        override fun setDetailVisibility(view: View, visibility: Boolean) {
            with(view) {
                when (visibility) {
                    false -> {
                        log_detail.visibility = View.GONE
                    }
                    true -> {
                        log_detail.visibility = View.VISIBLE
                    }
                }
            }
        }

        fun bind(item: TransceiveLog, position: Int) = with(view) {
            super.bind(position)
            tag = position
            log_send.text = item.sendBytes.chunked(2).joinToString(" ")
            log_receive.text = item.receiveBytes.chunked(2).joinToString(" ")
            log_detail.text = item.detail
            setDetailVisibility(view, expanded[position])
        }
    }
}
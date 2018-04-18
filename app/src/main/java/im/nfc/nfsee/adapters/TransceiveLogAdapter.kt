package im.nfc.nfsee.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import im.nfc.nfsee.nfc.TransceiveLog
import kotlinx.android.synthetic.main.log_item.view.*

class TransceiveLogAdapter(private val items: List<TransceiveLog>, context: Context) : RecyclerView.Adapter<TransceiveLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                with(view.log_detail){
                    visibility = when(visibility) {
                        View.VISIBLE -> View.GONE
                        else -> View.VISIBLE
                    }
                }
            }
        }
        fun bind(item: TransceiveLog) = with(view) {
            log_send.text = item.sendBytes
            log_receive.text = item.receiveBytes
            log_detail.text = item.detail
        }
    }
}
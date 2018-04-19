package im.nfc.nfsee.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import im.nfc.nfsee.nfc.Transaction
import im.nfc.nfsee.nfc.TransactionType
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionAdapter(private val items: List<Transaction>, context: Context) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val dateFormat = android.text.format.DateFormat.getDateFormat(context)
    private val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

    private val expanded = MutableList(items.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], position)

    private fun getTransactionTypeString(type: TransactionType) = when (type) {
    // TODO use context.getText() later
        TransactionType.Load -> "充值"
        TransactionType.Purchase -> "支付"
        TransactionType.Unknown -> "未知"
    }

    inner class ViewHolder(view: View) : ExpandableViewHolder(view, expanded) {

        override fun setDetailVisibility(view: View, visibility: Boolean) {
            with(view) {
                when (visibility) {
                    false -> {
                        transaction_detail.visibility = View.GONE
                        transaction_indicator.setText(R.string.fa_angle_down)
                    }
                    true -> {
                        transaction_detail.visibility = View.VISIBLE
                        transaction_indicator.setText(R.string.fa_angle_up)
                    }
                }
            }
        }

        fun bind(item: Transaction, position: Int) = with(view) {
            super.bind(position)
            tag = position
            val date = item.datetime.toDate()
            transaction_date.text = dateFormat.format(date)
            transaction_time.text = timeFormat.format(date)
            transaction_county.text = item.merchant
            transaction_symbol.text = item.currency
            transaction_price.text = item.amount.toString()
            transaction_type.text = getTransactionTypeString(item.type)
        }
    }
}
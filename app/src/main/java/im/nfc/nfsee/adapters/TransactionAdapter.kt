package im.nfc.nfsee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.nfc.nfsee.R
import im.nfc.nfsee.models.Transaction
import im.nfc.nfsee.models.TransactionType
import im.nfc.nfsee.utils.DatetimeUtils.toLongDate
import im.nfc.nfsee.utils.DatetimeUtils.toLongTime
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionAdapter(private val items: List<Transaction>, context: Context)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val expanded = MutableList(items.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater
            .from(parent.context).inflate(R.layout.transaction_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], position)

    private fun getTransactionTypeString(type: TransactionType) = when (type) {
        // TODO use context.getText() later
        TransactionType.Load -> "充值"
        TransactionType.Purchase -> "消费"
        TransactionType.Unknown -> "未知"
    }

    inner class ViewHolder(view: View) : ExpandableViewHolder(view, expanded) {

        override fun setDetailVisibility(view: View, visibility: Boolean) {
            with(view) {
                when (visibility) {
                    false -> {
                        transaction_detail.visibility = View.GONE
                        transaction_indicator.setImageResource(
                                R.drawable.ic_keyboard_arrow_down_black_24dp)
                    }
                    true -> {
                        transaction_detail.visibility = View.VISIBLE
                        transaction_indicator.setImageResource(
                                R.drawable.ic_keyboard_arrow_up_black_24dp)
                    }
                }
            }
        }

        fun bind(item: Transaction, position: Int) = with(view) {
            super.bind(position)
            tag = position
            transaction_date.text = item.datetime.toLongDate()
            transaction_time.text = item.datetime.toLongTime()
            transaction_location.text = item.merchant
            transaction_symbol.text = item.currency

            val sign = when (item.type) {
                TransactionType.Load -> "+"
                TransactionType.Purchase -> "-"
                else -> ""
            }
            transaction_price.text = String.format("%s %d.%02d", sign,
                    item.amount / 100, item.amount % 100)

            transaction_type.text = getTransactionTypeString(item.type)
        }
    }
}
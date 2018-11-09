package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.TransactionAdapter
import im.nfc.nfsee.models.Transaction

import kotlinx.android.synthetic.main.fragment_tranactions.view.*

class CardTransactionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val transactions = arguments!!["transactions"] as List<Transaction>
        val transactionView = inflater.inflate(R.layout.fragment_tranactions, container, false)
        transactionView.list_history.layoutManager = LinearLayoutManager(this.context)
        transactionView.list_history.adapter = TransactionAdapter(transactions, this.context!!)
        return transactionView
    }
}
package im.nfc.nfsee.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.TransactionAdapter
import im.nfc.nfsee.nfc.Transaction
import im.nfc.nfsee.nfc.TransactionType

import kotlinx.android.synthetic.main.fragment_tranactions.view.*
import org.joda.time.LocalDateTime

class CardTransactionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var transactionView = inflater?.inflate(R.layout.fragment_tranactions, container, false)
        val testTransaction = Transaction(100, 100, LocalDateTime.now(), TransactionType.Purchase,"CNY", "Test", "Test", "Test")
        transactionView.list_history.layoutManager = LinearLayoutManager(this.context)
        transactionView.list_history.adapter = TransactionAdapter(listOf(testTransaction), this.context!!)
        return transactionView
    }
}
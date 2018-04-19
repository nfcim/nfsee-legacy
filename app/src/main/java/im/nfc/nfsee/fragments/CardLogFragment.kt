package im.nfc.nfsee.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.TransceiveLogAdapter
import im.nfc.nfsee.nfc.TransceiveLog
import kotlinx.android.synthetic.main.fragment_log.view.*

class CardLogFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val logView = inflater.inflate(R.layout.fragment_log, container, false)
        val testLog = TransceiveLog("de ad be ef", "ba de c0 ff ee")
        logView.list_log.layoutManager = LinearLayoutManager(this.context)
        logView.list_log.adapter = TransceiveLogAdapter(List(20){testLog})
        return logView
    }
}
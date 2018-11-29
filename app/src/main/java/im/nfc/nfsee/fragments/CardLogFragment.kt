package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.TransceiveLogAdapter
import im.nfc.nfsee.nfc.TransceiveLog
import kotlinx.android.synthetic.main.fragment_list.view.*

class CardLogFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val logs = arguments!!["logs"] as List<TransceiveLog>
        val logView = inflater.inflate(R.layout.fragment_list, container, false)
        logView.list.layoutManager = LinearLayoutManager(this.context)
        logView.list.adapter = TransceiveLogAdapter(logs)
        return logView
    }
}
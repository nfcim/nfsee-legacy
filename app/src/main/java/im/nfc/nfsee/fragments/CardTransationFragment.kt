package im.nfc.nfsee.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import kotlinx.android.synthetic.main.fragment_tranactions.*

class CardTransationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var transactionView = inflater?.inflate(R.layout.fragment_tranactions, container, false)
        // do something
        return transactionView
    }
}
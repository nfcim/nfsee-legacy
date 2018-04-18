package im.nfc.nfsee.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R

class CardLogFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var logView = inflater?.inflate(R.layout.fragment_log, container, false)
        // do something
        return logView
    }
}
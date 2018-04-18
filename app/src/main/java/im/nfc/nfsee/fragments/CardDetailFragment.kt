package im.nfc.nfsee.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.nfc.nfsee.R
import kotlinx.android.synthetic.main.fragment_card_detail.*

class CardDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var detailView = inflater?.inflate(R.layout.fragment_card_detail, container, false)
//        card_detail_title.text = "Test Card"
        return detailView
    }
}
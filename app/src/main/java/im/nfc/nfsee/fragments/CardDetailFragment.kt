package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import im.nfc.nfsee.R
import kotlinx.android.synthetic.main.fragment_card_detail.view.*

class CardDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val table = arguments!!["table"] as List<Pair<String, String>>
        val detailView = inflater.inflate(R.layout.fragment_card_detail, container, false)
        detailView.card_detail_title.text = arguments!!["title"] as String
        detailView.card_data_table.text = table.joinToString("\n") { p -> "${p.first}: ${p.second}" }
        return detailView
    }
}
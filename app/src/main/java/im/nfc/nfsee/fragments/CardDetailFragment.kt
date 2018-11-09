package im.nfc.nfsee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import im.nfc.nfsee.R
import kotlinx.android.synthetic.main.detail_table_row.view.*
import kotlinx.android.synthetic.main.fragment_card_detail.view.*

class CardDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val table = arguments!!["table"] as List<Pair<String, String>>
        val detailView = inflater.inflate(R.layout.fragment_card_detail,
                container, false)

        detailView.card_detail_title.text = arguments!!["title"] as String

        with(detailView.card_data_table) {
            table.forEach { p ->
                val tableRow = inflater.inflate(R.layout.detail_table_row,
                        this, false)
                this.addView(tableRow)
                tableRow.detail_row_name.text = p.first
                tableRow.detail_row_value.text = p.second
            }
        }


        with (detailView.card_image) {
            when (arguments!!["imageId"] as Int) {
                0 -> visibility = View.GONE
                else -> {
                    visibility = View.VISIBLE
                    setImageResource(arguments!!["imageId"] as Int)
                }
            }
        }

        return detailView
    }
}
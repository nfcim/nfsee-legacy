package im.nfc.nfsee.activities

import androidx.core.os.bundleOf
import im.nfc.nfsee.fragments.CardDetailFragment
import im.nfc.nfsee.fragments.CardLogFragment
import im.nfc.nfsee.fragments.CardTransactionFragment
import im.nfc.nfsee.models.CardData

class CardInfoActivity : ViewPagerActivity(
        "卡片详情",
        listOf("详细信息", "交易记录", "传输日志")
) {

    override fun initFragments() {
        val card = intent.extras!!["data"] as CardData

        val detailFragment = CardDetailFragment()
        detailFragment.arguments = bundleOf(
                "title" to card.title, "table" to card.table, "imageId" to card.imageId)
        fragments.add(detailFragment)

        val transactionFragment = CardTransactionFragment()
        transactionFragment.arguments = bundleOf("transactions" to card.transactions)
        fragments.add(transactionFragment)

        val cardLogFragment = CardLogFragment()
        cardLogFragment.arguments = bundleOf("logs" to card.transceiveLogs)
        fragments.add(cardLogFragment)
    }
}
package im.nfc.nfsee.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.FragmentPagerAdapter
import im.nfc.nfsee.fragments.CardDetailFragment
import im.nfc.nfsee.fragments.CardLogFragment
import im.nfc.nfsee.fragments.CardTransactionFragment
import im.nfc.nfsee.nfc.CardData
import kotlinx.android.synthetic.main.activity_card_info.*

class CardInfoActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        info_viewpager.currentItem = tab!!.position
    }

    private var fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        initFragments()
        info_viewpager.adapter = FragmentPagerAdapter(fragments, listOf("详细信息", "交易记录", "日志"), supportFragmentManager)
        info_tabLayout.setupWithViewPager(info_viewpager)
        info_tabLayout.addOnTabSelectedListener(this)
    }

    private fun initFragments() {
        val card = intent.extras!!["data"] as CardData

        val detailFragment = CardDetailFragment()
        detailFragment.arguments = bundleOf("title" to card.title, "table" to card.table)
        fragments.add(detailFragment)

        val transactionFragment = CardTransactionFragment()
        transactionFragment.arguments = bundleOf("transactions" to card.transactions)
        fragments.add(transactionFragment)

        fragments.add(CardLogFragment())
    }
}
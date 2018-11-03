package im.nfc.nfsee.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import im.nfc.nfsee.R
import im.nfc.nfsee.adapters.FragmentPagerAdapter
import im.nfc.nfsee.fragments.CardDetailFragment
import im.nfc.nfsee.fragments.CardLogFragment
import im.nfc.nfsee.fragments.CardTransactionFragment
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
        fragments.add(CardDetailFragment())
        fragments.add(CardTransactionFragment())
        fragments.add(CardLogFragment())
    }
}
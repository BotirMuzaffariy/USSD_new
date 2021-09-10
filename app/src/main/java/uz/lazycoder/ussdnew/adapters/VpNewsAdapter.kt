package uz.lazycoder.ussdnew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.lazycoder.ussdnew.ui.news.VpNewsFragment
import uz.lazycoder.ussdnew.ui.news.VpSaleFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpNewsAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) VpNewsFragment() else VpSaleFragment()
    }

}